import streamlit as st
from streamlit_chat import message
from streamlit.components.v1 import html
import os
from langchain_openai.chat_models import ChatOpenAI
from dotenv import load_dotenv
import google.generativeai as gen_ai
from groq import Groq
import pandas as pd
from PyPDF2 import PdfReader

# Load environment variables
load_dotenv()

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
if "selected_ai" not in st.session_state:
    st.session_state.selected_ai = "OpenAI"  # Default API
if "file_uploaded" not in st.session_state:
    st.session_state.file_uploaded = False
if "file_name" not in st.session_state:
    st.session_state.file_name = None
if "data_frame" not in st.session_state:
    st.session_state.data_frame = None
if "pdf_content" not in st.session_state:
    st.session_state.pdf_content = ""
if "past" not in st.session_state:
    st.session_state.past = []
if "generated" not in st.session_state:
    st.session_state.generated = []

# Function to reset file-related states
def reset_file_state():
    st.session_state.file_uploaded = False
    st.session_state.file_name = None
    st.session_state.data_frame = None
    st.session_state.pdf_content = ""

# Function to process CSV files
def process_csv(file):
    try:
        df = pd.read_csv(file)
        st.session_state.data_frame = df
        return df
    except Exception as e:
        st.error(f"Error reading CSV: {e}")
        return None

# Function to process PDF files
def process_pdf(file):
    try:
        pdf_reader = PdfReader(file)
        text = ""
        for page in pdf_reader.pages:
            text += page.extract_text() or ""
        st.session_state.pdf_content = text
        return text
    except Exception as e:
        st.error(f"Error reading PDF: {e}")
        return ""

# Function to handle uploaded files
def handle_uploaded_file(uploaded_file):
    file_type = uploaded_file.name.split(".")[-1].lower()
    if file_type == "csv":
        df = process_csv(uploaded_file)
        if df is not None:
            st.session_state.file_uploaded = True
    elif file_type == "pdf":
        pdf_text = process_pdf(uploaded_file)
        if pdf_text:
            st.session_state.file_uploaded = True
    else:
        st.error("Unsupported file format. Please upload a CSV or PDF file.")

# Function to generate response from AI
def generate_response(input_text):
    response_text = ""

    # Check if file is uploaded and data is available
    if st.session_state.file_uploaded:
        if st.session_state.data_frame is not None:
            input_text += "\n\nData Summary:\n" + str(st.session_state.data_frame.describe())
        elif st.session_state.pdf_content:
            input_text += "\n\nExtracted PDF Content:\n" + st.session_state.pdf_content[:1000]  # Limit for performance

    if st.session_state.selected_ai == "OpenAI":
        model = ChatOpenAI(temperature=0.7, api_key=openai_api_key)
        response = model.invoke(input_text)
        response_text = response.content
    elif st.session_state.selected_ai == "Gemini":
        model = gen_ai.GenerativeModel('gemini-pro')
        response = model.generate_content(input_text)
        response_text = response.text
    elif st.session_state.selected_ai == "Llama" and llama_client:
        messages = [{"role": "user", "content": input_text}]
        response = llama_client.chat.completions.create(
            model="llama-3.1-70b-versatile",
            messages=messages
        )
        response_text = response.choices[0].message.content
    else:
        response_text = "API not configured or invalid selection."

    # Append valid response to history
    if response_text:
        st.session_state.response_history.append({"role": f"{st.session_state.selected_ai}", "text": response_text})
    else:
        st.warning("Failed to get a valid response.")

# Callback to handle user input change
def on_input_change():
    user_input = st.session_state.user_input
    st.session_state.past.append(user_input)
    # Generate a response (replace the dummy response with actual logic)
    generate_response(user_input)
    response = st.session_state.response_history[-1]['text'] if st.session_state.response_history else "No response"
    st.session_state.generated.append({"type": "normal", "data": response})

# Callback to clear the chat history
def on_btn_click():
    st.session_state.past.clear()
    st.session_state.generated.clear()

# Page layout
st.set_page_config(layout="wide")
st.subheader("Chattera")

# Sidebar for selecting AI API
with st.sidebar:
    st.header("Chattera")
    uploaded_file = st.file_uploader("Upload your file (CSV or PDF):", type=["csv", "pdf"])
    if uploaded_file:
        reset_file_state()  # Reset state before processing new file
        st.session_state.file_name = uploaded_file.name
        handle_uploaded_file(uploaded_file)
    st.selectbox("Choose an AI:", ["OpenAI", "Gemini", "Llama"], key="selected_ai")

# Apply theme CSS
st.markdown(
    f"""
    <style>
    .stApp {{
        background-color: {"#333333"};
        color: {"white"};
    }}
    .scrollable {{
        max-height: 300px;
        overflow-y: auto;
        padding: 10px;
        background-color: {"#444444"};
        border: 1px solid #ccc;
        color: {"white"};
    }}
    </style>
    """,
    unsafe_allow_html=True
)

# Layout with two main columns
col1, col2 = st.columns([2, 1])

# File viewer section (2/3 of the space)
with col1:
    if st.session_state.file_uploaded:
        if st.session_state.data_frame is not None:
            st.dataframe(st.session_state.data_frame, height=600)
        elif st.session_state.pdf_content:
            st.text_area("PDF Content:", st.session_state.pdf_content, height=600)

# Chat interface section (1/3 of the space)
with col2:
    st.markdown('<div class="scrollable">', unsafe_allow_html=True)
    chat_placeholder = st.empty()

    with chat_placeholder.container():
        for i in range(len(st.session_state["generated"])):
            # Display user messages
            message(st.session_state["past"][i], is_user=True, key=f"{i}_user")
            # Display AI responses
            message(
                st.session_state["generated"][i]["data"],
                key=f"{i}",
                allow_html=True,
            )
    st.markdown('</div class="scrollable">', unsafe_allow_html=True)
    # Input area for user messages
    st.text_input("User Input:", on_change=on_input_change, key="user_input")
