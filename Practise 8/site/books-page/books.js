synchronize();

class Book {
    constructor(id, name, author, year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }
}

let changingId = null;

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

function synchronize() {
    let xhr = new XMLHttpRequest();
    let request = 'books/list';
    if (request === '') return;

    xhr.open('GET', request, true);
    xhr.onload = () => {
        if (xhr.status === 200) {
            let books = JSON.parse(xhr.responseText);

            let table = $('#books-table');
            table.empty().append(headers);

            for (let i = 0; i < books.length; ++i) {
                let row = createRow().attr('id', `book${books[i].id}`);
                row.append(createCell(books[i].id    ).attr('id',     `id${books[i].id}`));
                row.append(createCell(books[i].name  ).attr('id',   `name${books[i].id}`));
                row.append(createCell(books[i].author).attr('id', `author${books[i].id}`));
                row.append(createCell(books[i].year  ).attr('id',   `year${books[i].id}`));
                row.append(createCell(createControlButtons(books[i].id)));
                table.append(row);
            }
        }
    }
    xhr.send();
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

function buttonClick() {
    if (changingId == null) {
        addNewBook();
    } else {
        changeBookById(changingId);
    }
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

const createRow  = (context) => $('<tr>').html(context);
const createCell = (context) => $('<td>').html(context);
const createControlButtons = (id) => createRemoveBtn(id) + createEditBtn(id);
const createRemoveBtn = (id) => "<button class=\"simple-button neg\" onclick=\"removeBookById("+id+")\">Удалить</button>";
const createEditBtn   = (id) => "<button class=\"simple-button pos\" onclick=\"setChangingMode("+id+")\">Изменить</button>";

const headers = "<tr class=\"header\">\n" +
    "  <td>ID</td>\n" +
    "  <td>Название книги</td>\n" +
    "  <td>Автор книги</td>\n" +
    "  <td>Год написания</td>\n" +
    "  <td>Действия</td>\n" +
    "</tr>"
