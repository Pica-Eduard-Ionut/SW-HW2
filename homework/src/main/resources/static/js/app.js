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
    let contextStarters = [];

    const path = window.location.pathname;
    if (path.includes("index") || path === "/") {
        contextStarters = ["How do I upload an RDF?", "What formats are supported?"];
    } else if (path.includes("books")) {
        contextStarters = ["Recommend a sci-fi book", "Sort books by author", "Find books for beginners"];
    } else if (path.includes("graph")) {
        contextStarters = ["What does this graph show?", "How are nodes connected?"];
    } else {
        contextStarters = ["Hello!", "Help me find a book"];
    }

    contextStarters.forEach(text => {
        let btn = document.createElement('button');
        btn.className = 'starter-btn';
        btn.innerText = text;
        btn.onclick = () => {
            document.getElementById('chat-input').value = text;
        };
        startersContainer.appendChild(btn);
    });
});

function sendMessage() {
    const input = document.getElementById('chat-input');
    const log = document.getElementById('chat-log');
    if (input.value.trim() !== "") {
        log.innerHTML += `<p><strong>You:</strong> ${input.value}</p>`;
        log.innerHTML += `<p><strong>Bot:</strong> <i>(Backend integration pending)</i> I received your message!</p>`;
        input.value = '';
        document.getElementById('chatbot-messages').scrollTop = document.getElementById('chatbot-messages').scrollHeight;
    }
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