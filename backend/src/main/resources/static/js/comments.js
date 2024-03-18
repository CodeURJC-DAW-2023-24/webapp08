async function sendComment(){
    let comment = document.getElementById('comment').value;
    if (comment != ""){ 

    let id = document.getElementById('id').value;

    let csrfToken = document.querySelector('input[name="_csrf"]').value;

    const RESPONSE = await fetch( `/sendComment?comentario=${comment}&id=${id}`,{
            method: 'POST',
            headers: { 'X-XSRF-TOKEN': csrfToken }

          });
     
    let comments = await RESPONSE.json();
    document.getElementById('comment').value="";
    addComments(comments)
    }
    }

    function addComments(comment) {
      let containerMessage = document.getElementById('container-messages');    
    
        

        let p = document.createElement('p');
        let b = document.createElement('b');
        b.textContent = comment.name + ": ";
        p.appendChild(b);
        p.appendChild(document.createTextNode(comment.content));
        p.style.color="black"
        p.style.paddingLeft = "2px"
        containerMessage.appendChild(p);
  
      
  }
  
    