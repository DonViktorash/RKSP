function getFilter() {
    let xhr = new XMLHttpRequest();
    let request = $(".input-request")[0].value;
    if (request === "") return;

    xhr.open('GET', 'task/' + request, true);
    xhr.onload = () => {
        if (xhr.status === 200) {
            $("#response-text")[0].innerHTML = xhr.responseText;
        }
    }
    xhr.send();
}
