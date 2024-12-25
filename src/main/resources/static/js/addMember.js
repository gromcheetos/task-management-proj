async function searchMembers() {
    const keyword = document.getElementById('searchInput').value;
    const resultsDiv = document.getElementById('autocompleteResults');

    if (keyword.trim() === "") {
        resultsDiv.innerHTML = "";
        return;
    }

    try {
        // Fetch search results
        const response = await fetch(`/search/members?keyword=${encodeURIComponent(keyword)}`);
        const users = await response.json();

        // Debug: Log the response
        console.log("Response from API:", users);

        // Ensure users is an array
        if (!Array.isArray(users)) {
            throw new Error("API response is not an array");
        }

        // Clear previous results
        resultsDiv.innerHTML = "";

        // Populate results dynamically
        users.forEach(user => {
            const div = document.createElement('div');
            div.className = 'autocomplete-item';
            div.textContent = `${user.name} (${user.email})`;
            div.onclick = () => selectMember(user.name, user.email);
            resultsDiv.appendChild(div);
        });
    } catch (error) {
        console.error("Error fetching members:", error);
    }
}
