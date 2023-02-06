// ビューの再読み込みをすることなく特定の要素のみを更新する。
/*
パラメーター
・id: 指定したidがコントローラーにPOSTされる。
・name: 指定したnameがコントローラーにPOSTされる。
・targetElementId: 更新対象の要素IDを指定する。

使用方法
1. 対象の要素にフラグメント属性を付与する。
2. このメソッドを使用して、id をコントローラーに引き渡す。
3. IDに基づいてエンティティを再取得する。
4. 取得したエンティティをモデルに追加する。
5. return時に 1 で付与したフラグメントを指定する。
*/

function updateElementById(id, name, targetElementId) {
    
    // CSRF
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
    });

    // 更新対象の要素を取得
    var targetElement = document.getElementById(targetElementId);

    // データをPOSTする
    $.ajax({
        method: 'POST',
        data: {
            id: id,
            name: name
        },
        url: location.pathname
    }).then(
        function(data) {
            targetElement.outerHTML = data;
        },
        function() {
            alert("error");
        });
}