function editar(){
    let name = document.getElementById('name');
    let firstName = document.getElementById('firstName');
    let date = document.getElementById('date');
    let weight = document.getElementById('weight');
    let edit = document.getElementById('edit');
    let save = document.getElementById('save');
    edit.style.display="none";
    save.style.display = "block";
    name.readOnly = false;
    firstName.readOnly = false;
    date.readOnly = false;
    weight.readOnly = false;


}

function mostrar(){
    let edit = document.getElementById('edit');
    let save = document.getElementById('save');
    edit.style.display="block";
    save.style.display = "none";
}