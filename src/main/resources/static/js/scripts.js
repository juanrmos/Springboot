function actorSelection(select) {
    let index = select.selectedIndex;
    let option = select.options[index];
    let id = option.value;
    let nombre = option.text; // CORREGIDO: era 'number'
    let urlImagen = option.dataset.url;

    option.disabled = true; // CORREGIDO: sintaxis simplificada
    select.selectedIndex = 0; // CORREGIDO: era 'disabledIndex'

    agregarActor(id, nombre, urlImagen); // CORREGIDO: ahora 'nombre' está definido

    let ids = $("#ids").val();

    if (ids == "") {
        $("#ids").val(id);
    } else {
        $("#ids").val(ids + "," + id); // CORREGIDO: usar coma consistentemente
    }
}

function agregarActor(id, nombre, urlImagen) {
    let htmlString = `
        <div class="card col-md-3 m-2" style="width: 10rem">
            <img src="${urlImagen}" class="card-img-top" alt="${nombre}">
            <div class="card-body">
                <p class="card-text">${nombre}</p>
                <button type="button" class="btn btn-danger" data-id="${id}" onClick="eliminarActor(this); return false;">Eliminar</button>
            </div>
        </div>`;
    // CORREGIDO: usar template literals en lugar de replace

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