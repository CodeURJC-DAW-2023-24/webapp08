async function enviarComentario(){
    let comentario = document.getElementById('comentario').value;
    if (comentario != ""){ //Revisar si devuelve null o ""

    let id = document.getElementById('id').value;
    const response = await fetch( `/enviarComentario?comentario=${comentario}&id=${id}`,{
            method: 'POST'
          });
     
    let mensajes = await response.json();
    console.log(mensajes)
    }
    }

    //recargarComentarios()
