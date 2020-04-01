function post() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": commentContent,
            "type": 1,
        }),
        success: function (response) {
            if (response.code === 200) {
                $("#comment_section").hide();
            } else {
                if (response.code === 2003) {
                    var isNeededLogin = confirm(response.message);
                    if (isNeededLogin) {
                        window.open("https://github.com/login/oauth/authorize?client_id=3c8f29cbf1f1f7c41d59&redirect_uri=http://127.0.0.1:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closeable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}