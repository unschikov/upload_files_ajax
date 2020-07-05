$(document).ready( async function () {
    await renderMessages();
    $("button[type='submit']").on('click', function (e) {
        e.preventDefault()
        console.log('click create message')
        addMessage()
    })
})

async function addMessage() {
    const formData = new FormData();
    formData.append('title', $('#title').val())
    formData.append('content', $('#content').val())
    formData.append('file', $('#img').prop('files')[0])

    try {
        const response = await fetch('http://localhost:8080/api/message', {
            method: 'post',
            enctype: 'multipart/form-data',
            body: formData
        });
        if (response.ok) {
            const result = await response.json();
            console.log('Успех:', JSON.stringify(result));
            await renderMessages()
        }
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

async function renderMessages() {
    let response = await fetch("http://localhost:8080/api/message/all")
    if (response.ok) {
        let messages = await response.json()
        let content = ''
        messages.forEach(function (message) {
            content +=
                `<div class="card mb-3" style="max-width: 500px;">
                    <div class="row no-gutters">
                        <div class="col-md-4">
                            <img src="/img/${message.img}" class="card-img" alt="...">
                        </div>
                        <div class="col-md-8">
                            <div class="card-body">
                                <h5 class="card-title">${message.title}</h5>
                                <p class="card-text">${message.content}</p>
                                <p class="card-text"><small class="text-muted">Last updated 3 mins ago</small></p>
                            </div>
                        </div>
                    </div>
                </div>`
        })
        $('#container-message').html(content)
    } else {
        console.log("error -- " + response.status)
    }
}