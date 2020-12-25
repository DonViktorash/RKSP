class Book {
    constructor(id, name, author, year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }
}

let changingId = null;

function synchronize() {
    location.replace("/books");
}

function postRequest(parameters) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', 'books/post', true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.onload = () => {
        if (xhr.status === 200) {
            synchronize();
        }
    }
    xhr.send(parameters);
}

function removeBookById(id) {
    postRequest(JSON.stringify(new Book(
        id,
        null,
        null,
        null
    )));
}

function changeBookById(id) {
    let name   = $('.input-name'  )[0].value;
    let author = $('.input-author')[0].value;
    let year   = $('.input-year'  )[0].value;
    if (name === '' || author === '' || year === '') return;
    postRequest(JSON.stringify(new Book(
        id,
        name,
        author,
        year
    )));
    unsetChanging();
}

function addNewBook() {
    let name   = $('.input-name'  )[0].value;
    let author = $('.input-author')[0].value;
    let year   = $('.input-year'  )[0].value;
    if (name === '' || author === '' || year === '') return;
    postRequest(JSON.stringify(new Book(
        null,
        name,
        author,
        year
    )));
    unsetChanging();
}

function buttonClick() {
    if (changingId == null) {
        addNewBook();
    } else {
        changeBookById(changingId);
    }
}

function setChangingMode(id) {
    if (changingId != null) {
        $('#book'+changingId).attr("style", "");
    }
    if (changingId === id) {
        unsetChanging();
    } else {
        $('#book' + id).attr("style", "background: #2B2B2B");
        setChanging(id);
    }
}

function setChanging(id) {
    changingId = id;
    $('.button').html(`Изменить книгу [${id}]`);
    $('.input-name'  )[0].value = $(`#name${id}`  ).text();
    $('.input-author')[0].value = $(`#author${id}`).text();
    $('.input-year'  )[0].value = $(`#year${id}`  ).text();
}

function unsetChanging() {
    changingId = null;
    $('.button').html(`Добавить`);
    $('.input-name'  )[0].value = '';
    $('.input-author')[0].value = '';
    $('.input-year'  )[0].value = '';
}
