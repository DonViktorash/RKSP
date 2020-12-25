<#macro navigationBar active>
<ul class="navigation-bar">
    <li><a <#if active == "about">class="active" </#if>href="about">О себе</a></li>
    <li><a <#if active == "task" >class="active" </#if>href="task">Задание</a></li>
    <li><a <#if active == "books">class="active" </#if>href="books">Книги</a></li>
</ul>
</#macro>

<#macro tableAbout fullName, group, taskVariant, taskText>
<div class="about">
    <p>
        <span>Работу выполнил:</span>
        <b>${fullName}</b>
    </p>
    <p>
        <span>Номер группы:</span>
        <b>${group}</b>
    </p>
    <p>
        <td class="header">Номер индивидуального задания:</td>
        <b>${taskVariant}</b>
    </p>
    <p>
        <span>Текст индивидуального задания:</span>
        <b>${taskText}</b>
    </p>
</div>
</#macro>

<#-- form for add/update books -->
<#macro bookForm>
<div style="padding: 10px">
    <input class="input-name" type="text" name="input-name" placeholder="Название">
    <input class="input-author" type="text" name="input-author" placeholder="Автор">
    <input class="input-year" type="text" name="input-year" placeholder="Год написания">
    <button onclick="buttonClick()">Добавить</button>
</div>
</#macro>

<#-- form for task page -->
<#macro taskForm lastTaskRequest lastTaskResponse>
<div class="task">
    <input
            class="input-request"
            type="text"
            onchange="getCharStatistics()"
            name="input-request"
            value="${lastTaskRequest}"
            placeholder="Ваш запрос">
    <button onclick="getFilter()">Отправить</button>
</div>
    <div id="response-text">${lastTaskResponse}</div>
</#macro>

<#-- auto-generated table of books -->
<#macro booksTable booksList>
<table class="books-table" id="books-table" width="100%">
    <tr class="header">
        <td>ID</td>
        <td>Название книги</td>
        <td>Автор книги</td>
        <td>Год написания</td>
        <td>Действия</td>
    </tr>
    <#list booksList as book>
    <tr id="book${book.getId()}">
        <td id="id${book.getId()}">${book.getId()}</td>
        <td id="name${book.getId()}">${book.getName()}</td>
        <td id="author${book.getId()}">${book.getAuthor()}</td>
        <td id="year${book.getId()}">${book.getYear()}</td>
        <td>
            <button onclick="removeBookById(${book.getId()})">
                Удалить
            </button>
            <button onclick="setChangingMode(${book.getId()})">
                Изменить
            </button>
        </td>
    </tr>
</#list>
</table>
</#macro>
