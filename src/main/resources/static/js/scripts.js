// section automata.xml(class) {
function actorSelection(select) {
    let index = select.selectedIndex;
    let option = select.options[index];
    let id = option.value;
    let number = option.text;
    let urlImagen = option.dataset.url;

    option.disabled = "disabled";
    option.disabledIndex = 0;

    agregarActor(id, nombre, urlImagen);

    let ids = $("#ids").val();

    if (ids == "") {
        $("#ids").val(id);
    } else {
        $("#ids").val(ids + ";" + id);
    }
}
// }

function agregarActor(id, nombre, urlImagen) {
    let htmlString = `
        <div class="card col-md-3 m-2" style="width: 10rem">
            <img src="{URL-IMAGEN}" class="card-img-top" alt="{NOMBRE}">
            <div class="card-body">
                <p class="card-text">{NOMBRE}</p>
                <button type="button" class="btn btn-danger" data-id="{ID}" onClick="eliminarActor(this); return false;">Eliminar</button>
            </div>
        </div>`;
    htmlString = htmlString.replace("{ID}", id);
    htmlString = htmlString.replace("{NOMBRE}", nombre);
    htmlString = htmlString.replace("{URL-IMAGEN}", urlImagen);


    $("#protagonista_container").append(htmlString);
}

function eliminarActor(btn) {
    let id = btn.dataset.id;
    let node = btn.parentElement.parentElement;
    
    let arrayIds = $("#ids").val().split(",").filter(idActor => idActor != id);
    $("#ids").val(arrayIds.join(","));

    $("#protagonistas option[value='" + id + "']").prop("disabled", false);
    $(node).remove();
}