async function sendComment(){
    let comment = document.getElementById('comment').value;
    if (comment != ""){ 

    let id = document.getElementById('id').value;
    const response = await fetch( `/sendComment?comentario=${comment}&id=${id}`,{
            method: 'POST'
          });
     
    let comments = await response.json();
    addComments(comments)
    }
    }

    function addComments(comment) {
      let containerMessage = document.getElementById('container-messages');    
          let com = document.createElement('div');
          com.classList.add('com');
          com.style.display = 'flex'; 
          com.style.flexDirection = 'column'; 
          let pName = document.createElement('p');
          let bName = document.createElement('b');
          bName.textContent = comment.name;
          pName.appendChild(bName);
          let pMessage = document.createElement('p');
          pMessage.textContent = comment.content;
          com.appendChild(pName);
          com.appendChild(pMessage);
          containerMessage.appendChild(com);
      
  }
  
    
