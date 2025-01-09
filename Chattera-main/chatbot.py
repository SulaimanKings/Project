import streamlit as st
import pandas as pd
from PyPDF2 import PdfReader
from langchain_openai.chat_models import ChatOpenAI
from dotenv import load_dotenv
import os
import google.generativeai as gen_ai
from groq import Groq

# Load environment variables
load_dotenv()

# API Keys
openai_api_key = os.getenv("openai_api_key")
GOOGLE_API_KEY = os.getenv("GEMINI_API_KEY")
GROQ_API_KEY = os.getenv("llama_api_key")

# Configure Google Gemini API
if GOOGLE_API_KEY:
    gen_ai.configure(api_key=GOOGLE_API_KEY)

# Configure Llama API
llama_client = None
if GROQ_API_KEY:
    llama_client = Groq(api_key=GROQ_API_KEY)

# Initialize session state
if "response_history" not in st.session_state:
    st.session_state.response_history = []
if "uploaded_file_content" not in st.session_state:
    st.session_state.uploaded_file_content = ""
if "selected_api" not in st.session_state:
    st.session_state.selected_api = "OpenAI"  # Default API
if "file_data" not in st.session_state:
    st.session_state.file_data = None  # Store DataFrame for CSV files

# Function to generate response from input text or file content
def generate_response(input_text, selected_api):
    if selected_api == "OpenAI":
        model = ChatOpenAI(temperature=0.7, api_key=openai_api_key)
        response = model.invoke(input_text)
        response_text = response.content
    elif selected_api == "Gemini":
        model = gen_ai.GenerativeModel('gemini-pro')
        response = model.generate_content(input_text)
        response_text = response.text
    elif selected_api == "Llama" and llama_client:
        messages = [{"role": "user", "content": input_text}]
        response = llama_client.chat.completions.create(
            model="llama-3.1-70b-versatile",
            messages=messages
        )
        response_text = response.choices[0].message.content
    else:
        response_text = "API not configured or invalid selection."

    st.session_state.response_history.append({"api": selected_api, "response": response_text})
    st.write(response_text)

# Function to load and process a CSV file
def process_csv(file):
    try:
        df = pd.read_csv(file)
        st.session_state.file_data = df  # Save DataFrame to session state
        return df
    except Exception as e:
        st.error(f"Error reading CSV: {e}")
        return None

# Function to load and process a PDF file
def process_pdf(file):
    try:
        pdf_reader = PdfReader(file)
        text = ""
        for page in pdf_reader.pages:
            text += page.extract_text() or ""
        return text
    except Exception as e:
        st.error(f"Error reading PDF: {e}")
        return ""

# Layout configuration
st.set_page_config(layout="wide")
col1, col2 = st.columns([2, 3])

# Left column: File upload and spreadsheet
with col1:
    st.title("Spreadsheet Viewer")
    
    # Dropdown untuk memilih API
    st.selectbox("Choose AI API:", ["OpenAI", "Gemini", "Llama"], key="selected_api")
    
    # File uploader
    uploaded_file = st.file_uploader("Upload a CSV or PDF file", type=["csv", "pdf"])

    if uploaded_file is not None:
        file_extension = uploaded_file.name.split(".")[-1].lower()
        if file_extension == "csv":
            df = process_csv(uploaded_file)
            if df is not None:
                # Menampilkan spreadsheet interaktif
                st.dataframe(df.style.format(precision=2), use_container_width=True)
        elif file_extension == "pdf":
            file_text = process_pdf(uploaded_file)
            st.text_area("PDF Content:", value=file_text, height=300)

# Right column: Prompt and responses
with col2:
    st.title("Chattera")

    # Prompt input
    with st.form("prompt_form"):
        text = st.text_area("Enter your prompt:", "")
        submitted = st.form_submit_button("Submit")

        # Generate response based on selected AI and input
        if submitted:
            if st.session_state.selected_api == "OpenAI" and not openai_api_key.startswith("sk-"):
                st.warning("Please enter a valid OpenAI API key!", icon="⚠")
            elif st.session_state.selected_api == "Gemini" and not GOOGLE_API_KEY:
                st.warning("Please enter a valid Google API key!", icon="⚠")
            elif st.session_state.selected_api == "Llama" and not llama_client:
                st.warning("Please enter a valid Llama API key!", icon="⚠")
            else:
                if st.session_state.file_data is not None:
                    input_text = st.session_state.file_data.to_csv(index=False)  # Pass DataFrame as text
                    generate_response(input_text, st.session_state.selected_api)
                elif text.strip():
                    generate_response(text, st.session_state.selected_api)
                else:
                    st.warning("Please upload a file or enter a prompt.", icon="⚠")

    # Response history
    st.subheader("Response History")
    for past_response in st.session_state.response_history:
        st.write(f"**{past_response['api']}**: {past_response['response']}")
