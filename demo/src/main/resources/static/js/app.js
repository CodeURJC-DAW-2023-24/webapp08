function editar(){
    let name = document.getElementById('name');
    let firstName = document.getElementById('firstName');
    let date = document.getElementById('date');
    let weight = document.getElementById('weight');
    let edit = document.getElementById('edit');
    let save = document.getElementById('save');
    let image = document.getElementById('image');
    edit.style.display="none";
    save.style.display = "block";
    image.disabled = false;
    name.readOnly = false;
    firstName.readOnly = false;
    date.readOnly = false;
    weight.readOnly = false;


}

function changeEx(){
    let grupos = ['Pecho','Espalda','Hombro','Biceps','Triceps','Inferior','Cardio'];
    let grp = document.getElementById('grupo').value;
    grupos.forEach(function(grupo) {
        document.getElementById(grupo).style.display = "none";
    });
    let newGrp = document.getElementById(grp);
    newGrp.style.display="block";
}

