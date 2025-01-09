import streamlit as st
import streamlit.components.v1 as components

# Atur judul halaman
st.set_page_config(page_title="Scrollable Columns", layout="wide")

# CSS untuk membuat kolom dapat di-scroll
scrollable_css = """
<style>
.scrollable {
    height: 300px; /* Atur tinggi sesuai kebutuhan */
    overflow-y: scroll;
    border: 1px solid #ccc;
    padding: 10px;
    background-color: #f9f9f9;
}
</style>
"""

# Tambahkan CSS ke Streamlit
st.markdown(scrollable_css, unsafe_allow_html=True)

# Layout dua kolom
col1, col2 = st.columns(2)

with col1:
    st.markdown("### Kolom 1")
    # Area scrollable untuk kolom 1
    components.html(
        """
        <div class='scrollable'>
            <p>Konten 1 baris 1</p>
            <p>Konten 1 baris 2</p>
            <p>Konten 1 baris 3</p>
            <p>Konten 1 baris 4</p>
            <p>Konten 1 baris 5</p>
            <p>Konten 1 baris 6</p>
            <p>Konten 1 baris 7</p>
        </div>
        """,
        height=300,
    )

with col2:
    st.markdown("### Kolom 2")
    # Area scrollable untuk kolom 2
    components.html(
        """
        <div class='scrollable'>
            <p>Konten 2 baris 1</p>
            <p>Konten 2 baris 2</p>
            <p>Konten 2 baris 3</p>
            <p>Konten 2 baris 4</p>
            <p>Konten 2 baris 5</p>
            <p>Konten 2 baris 6</p>
            <p>Konten 2 baris 7</p>
        </div>
        """,
        height=300,
    )
