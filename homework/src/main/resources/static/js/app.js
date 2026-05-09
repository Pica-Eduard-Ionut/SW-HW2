document.addEventListener("DOMContentLoaded", () => {
    const chatHTML = `
        <div id="chatbot-container">
            <div id="chatbot-header">
                <span>🤖 BookBot</span>
                <span id="chat-toggle">▼</span>
            </div>
            <div id="chatbot-messages">
                <div class="starters" id="chat-starters"></div>
                <div id="chat-log"></div>
            </div>
            <div id="chatbot-input-area">
                <input type="text" id="chat-input" placeholder="Ask something...">
                <button onclick="sendMessage()">Send</button>
            </div>
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', chatHTML);

    const toggleBtn = document.getElementById('chatbot-header');
    const messagesDiv = document.getElementById('chatbot-messages');
    const inputArea = document.getElementById('chatbot-input-area');

    toggleBtn.addEventListener('click', () => {
        const isHidden = messagesDiv.style.display === 'none' || messagesDiv.style.display === '';
        messagesDiv.style.display = isHidden ? 'block' : 'none';
        inputArea.style.display = isHidden ? 'block' : 'none';
        document.getElementById('chat-toggle').innerText = isHidden ? '▲' : '▼';
    });

    const startersContainer = document.getElementById('chat-starters');

    const path = window.location.pathname;
    const params = new URLSearchParams(window.location.search);
    let page = 'home';
    if (path.includes('book-details')) page = 'bookDetail';
    else if (path.includes('books')) page = 'books';

    const bookId = params.get('id') || '';
    const startersUrl = `/api/chat/starters?page=${page}&bookId=${encodeURIComponent(bookId)}`;

    fetch(startersUrl)
        .then(r => r.json())
        .then(data => {
            (data.starters || []).forEach(text => {
                const btn = document.createElement('button');
                btn.className = 'starter-btn';
                btn.innerText = text;
                btn.onclick = () => { document.getElementById('chat-input').value = text; };
                startersContainer.appendChild(btn);
            });
        })
        .catch(() => {});
});

async function sendMessage() {
    const input = document.getElementById('chat-input');
    const log = document.getElementById('chat-log');
    const message = input.value.trim();
    if (!message) return;

    log.innerHTML += `<p><strong>You:</strong> ${message}</p>`;
    input.value = '';

    const params = new URLSearchParams(window.location.search);
    try {
        const res = await fetch('/api/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message,
                pageContext: window.location.pathname,
                bookId: params.get('id') || ''
            })
        });
        const data = await res.json();
        log.innerHTML += `<p><strong>Bot:</strong> ${data.reply || 'No response received.'}</p>`;
    } catch (e) {
        log.innerHTML += `<p><strong>Bot:</strong> Sorry, I could not connect to the server.</p>`;
    }
    document.getElementById('chatbot-messages').scrollTop = document.getElementById('chatbot-messages').scrollHeight;
}

async function loadBooks() {
    try {
        const response = await fetch('/api/books'); 
        const books = await response.json();
        
        const grid = document.querySelector('.book-grid');
        grid.innerHTML = ''; 

        books.forEach(book => {
            grid.innerHTML += `
                <div class="book-card">
                    <h3>${book.title}</h3>
                    <p><strong>Author:</strong> ${book.author}</p>
                    <p><strong>Themes:</strong> ${book.themes.join(', ')}</p>
                    <p><strong>Level:</strong> ${book.readingLevel}</p>
                    <a href="book-details.html?id=${book.title}">View Details</a>
                </div>
            `;
        });
    } catch (error) {
        console.error("Nu am putut încărca datele din RDF:", error);
    }
}