document.addEventListener('DOMContentLoaded', () => {
	const content = document.getElementById('content');
	const button_bold = document.getElementById('button-bold');
	
	button_bold.addEventListener('click', (event) => {
		console.log("bold button clicked")
		event.preventDefault();
		
	    const start = content.selectionStart;
	    const end = content.selectionEnd;
	
	    const text = content.value;
	    const selected_text = text.substring(start, end);
	
	    if (selected_text) {
	        content.value = text.substring(0, start) + `[b]${selected_text}[/b]` + text.substring(end);
	    } else {
	        content.value = text.substring(0, start) + `[b][/b]` + text.substring(start);
	        // Move the tags to the center between both tags [b][/b]
	        content.selectionStart = content.selectionEnd = start + 3;
	    }
	
		// Focus again the text area to continue editing
	    content.focus();
	});
});