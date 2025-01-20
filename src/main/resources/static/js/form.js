document.addEventListener('DOMContentLoaded', () => {
	const content = document.getElementById('content');
	const button_bold = document.getElementById('button-bold');
	const button_italic = document.getElementById('button-italic');
	
	button_bold.addEventListener('click', (event) => {
		console.log("bold button clicked")
		event.preventDefault();
		add_tags(content, 'b')
	});

	button_italic.addEventListener('click', (event) => {
		console.log("bold italic clicked")
		event.preventDefault();
		add_tags(content, 'i')
	});
		
	function add_tags(content, tag) {
		const start = content.selectionStart;
		const end = content.selectionEnd;

		const text = content.value;
		const selected_text = text.substring(start, end);

		if (selected_text) {
		    content.value = text.substring(0, start) + `[` + tag + `]${selected_text}[/` + tag + `]` + text.substring(end);
		} else {
		    content.value = text.substring(0, start) + `[` + tag + `][/` + tag + `]` + text.substring(start);
		    // Move the tags to the center between both tags
		    content.selectionStart = content.selectionEnd = start + 3;
		}

		// Focus again the text area to continue editing
		content.focus();
	}
});