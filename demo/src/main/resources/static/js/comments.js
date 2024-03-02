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
      let containerMensajes = document.getElementById('container-messages');    
          let com = document.createElement('div');
          com.classList.add('com');
          com.style.display = 'flex'; 
          com.style.flexDirection = 'column'; 
          // Create user imagen 
          let img = document.createElement('img');
         // img.src = comment.imagen;
         // img.classList.add('rutine-image');
  
          // Crear el nombre del usuario (en negrita)
          let pNombre = document.createElement('p');
          let bNombre = document.createElement('b');
          bNombre.textContent = comment.nombre;
          pNombre.appendChild(bNombre);
  
          // Create user mensage
          let pMensaje = document.createElement('p');
          pMensaje.textContent = comment.content;
  
          // Add imagen,name and mesage
         

          com.appendChild(img);
          com.appendChild(pNombre);
          
          com.appendChild(pMensaje);
  
          // Add comment to the mensage container
          containerMensajes.appendChild(com);
      
  }
  
    
