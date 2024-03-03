function editUser(){
    let name = document.getElementById('name');
    let alias = document.getElementById('alias');
    let date = document.getElementById('date');
    let weight = document.getElementById('weight');
    let edit = document.getElementById('edit');
    let save = document.getElementById('save');
    let image = document.getElementById('image');
    edit.style.display="none";
    save.style.display = "block";
    image.disabled = false;
    name.readOnly = false;
    alias.readOnly = false;
    date.readOnly = false;
    weight.readOnly = false;


}

function changeEx(){
    let grps = ['Pecho','Espalda','Hombro','Biceps','Triceps','Inferior','Cardio'];
    let grp = document.getElementById('group').value;
    grps.forEach(function(group) {
        document.getElementById(group).style.display = "none";
    });
    let newGrp = document.getElementById(grp);
    newGrp.style.display="block";
}

