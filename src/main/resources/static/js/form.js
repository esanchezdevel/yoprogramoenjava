document.addEventListener('DOMContentLoaded', () => {
	const content = document.getElementById('content');
	const button_bold = document.getElementById('button-bold');
	const button_italic = document.getElementById('button-italic');
	const button_image = document.getElementById('button-image');
	const button_youtube = document.getElementById('button-youtube');
	const button_tweet = document.getElementById('button-tweet');
	
	button_bold.addEventListener('click', (event) => {
		event.preventDefault();
		add_tags(content, 'b')
	});

	button_italic.addEventListener('click', (event) => {
		event.preventDefault();
		add_tags(content, 'i')
	});

	button_image.addEventListener('click', (event) => {
		event.preventDefault();
		add_tags(content, 'img')
	});

	button_youtube.addEventListener('click', (event) => {
		event.preventDefault();
		add_tags(content, 'youtube')
	});
	
	button_tweet.addEventListener('click', (event) => {
		event.preventDefault();
		add_tags(content, 'tweet')
	});
				
	function add_tags(content, tag) {
		const start = content.selectionStart;
		const end = content.selectionEnd;

		const text = content.value;
		const selected_text = text.substring(start, end);

		if (selected_text) {
			if (tag === 'img' || tag === 'youtube' || tag === 'tweet') {
				content.value = text.substring(0, end) + `[` + tag + ` src='']` + text.substring(end);
			} else {
				content.value = text.substring(0, start) + `[` + tag + `]${selected_text}[/` + tag + `]` + text.substring(end);	
			}
		} else {
			if (tag === 'img' || tag === 'youtube' || tag === 'tweet') {
				content.value = text.substring(0, start) + `[` + tag + ` src='']` + text.substring(start);
				content.selectionStart = content.selectionEnd = start;
			} else {
				content.value = text.substring(0, start) + `[` + tag + `][/` + tag + `]` + text.substring(start);
				// Move the tags to the center between both tags
				content.selectionStart = content.selectionEnd = start + 3;	
			}		    
		}

		// Focus again the text area to continue editing
		content.focus();
	}
});