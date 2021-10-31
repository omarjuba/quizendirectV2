//creation de l'editeur QUillJs

var quill = new Quill('#editeurQuill-enonce', {
    modules: {
      toolbar: [
        [{'font': ['monospace',true]},{ header: [1, 2, 3, false] }],
        ['bold', 'italic', 'underline','strike'],
        [{ 'color': [] }, { 'background': [] },'blockquote'],
        ['video'],
        [{ 'indent': '-1'}, { 'indent': '+1' }], 
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'script': 'sub'}, { 'script': 'super' }],
      ]
    },
    placeholder: 'Ecrivez votre question...',
    theme: 'snow' 

    
  });