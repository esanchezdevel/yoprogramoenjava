document.addEventListener("DOMContentLoaded", function () {
    // Define keyword groups with different styles
    const keywordGroups = {
        accessModifiers: ["class", "interface", "record", "implements", "extends", "public", "private", "protected", 
            "for", "if", "else", "elif", "enum", "while", "import", "package"],
        dataTypes: ["String", "Integer", "int", "long", "Long", "double", "float", "Float", "List", "ArrayList", "Map", "HashMap", "Set", "HashSet"]
    };

    // Function to highlight keywords with appropriate class
    function highlightCode(codeBlock) {
        let html = codeBlock.innerHTML;

        // Loop through each group
        for (const group in keywordGroups) {
            // Loop through each keyword in the group
            keywordGroups[group].forEach((keyword) => {
                const regex = new RegExp(`\\b${keyword}\\b`, "g"); // Match whole words
                // Replace keyword with <span> wrapped around it and apply group class
                html = html.replace(regex, `<span class="${group}">${keyword}</span>`);
            });
        }

        codeBlock.innerHTML = html;
    }

    // Apply to all <code> blocks
    document.querySelectorAll(".code-block").forEach(highlightCode);
});
