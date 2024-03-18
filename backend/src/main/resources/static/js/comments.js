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

        let dltBtn = document.createElement('button');
        dltBtn.classList.add("btn", "btn-danger", "btn-sm", "ml-2","float-right");
        dltBtn.textContent = 'Borrar';
        
       



        dltBtn.addEventListener('click', function() {
          let csrfToken = document.querySelector('input[name="_csrf"]').value;
          let id = document.getElementById("id").value;
           fetch( `/deleteComment?commentId=${comment.id}&rutineId=${id}`,{ 
                  method: 'POST',
                  headers: { 'X-XSRF-TOKEN': csrfToken }
                });
            containerMessage.removeChild(p);   
      });
      p.appendChild(dltBtn);
    
  }
  
    
async function deleteComment(event, idComentario){

  let idRutina = document.getElementById("id").value;

  let csrfToken = document.querySelector('input[name="_csrf"]').value;
  fetch( `/deleteComment?commentId=${idComentario}&rutineId=${idRutina}`,{ 
         method: 'POST',
         headers: { 'X-XSRF-TOKEN': csrfToken }
       });
       let paragraph = event.target.closest('p');
       if (paragraph) {
         paragraph.remove();
       }
     
}