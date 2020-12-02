// const buttonDelete = document.querySelector('input')

function ShowMessage(stName) {
    alert("I'm "+stName);
}

// function UpdateName() {
//     const str = prompt("Insert data:");
//     alert("You input "+str);
//     return str;
// }

function deleteRow(btn) {
    var row = btn.parentNode.parentNode;
    row.parentNode.removeChild(row);
}

function addRow() {
    var table = document.getElementById("tableId");
    var row = table.insertRow(1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    cell1.innerHTML = "New Name";
    cell2.innerHTML = "New LastName";
}