<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html style="background:rgb(233, 246, 236);">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ニコニコ動画風のコメント表示</title>
    <!-- Google Fonts の接続設定 -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <!-- Google Fonts の読み込み -->
    <link href="https://fonts.googleapis.com/css2?family=Righteous&display=swap" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.5.1/gsap.min.js"></script>
<style>

.container{
	background-color: gray;
}

/* Googleフォントを適用 */
.flowing-text {
    font-family: 'Righteous', cursive; /* Google Fonts 'Righteous' を適用 */
    color:  #28a745; /* 緑色に設定 */
}

/* 画像ボタンのスタイル */
.image-button {
    width: 100px; /* ボタンの幅を設定（必要に応じて調整） */
    height: 100px; /* ボタンの高さを設定（必要に応じて調整） */
    border-radius: 50%; /* 円形にする */
    border: none; /* ボタンのボーダーを削除 */
    cursor: pointer; /* ポインタがボタンであることを示すカーソル */
}

</style>

</head>

<body>
<div class="container">
  <button id="button01" class="image-button">
    <img src="/imageUpload/img/favi/BOOK.jpg" alt="Genau Button" style="width: 100%; height: 100%; border-radius: 50%;">
  </button>
</div>
<script>
const button01 = document.getElementById("button01");
button01.addEventListener("click", function(){createText()}, false);
let count = 0;

async function createText() {
  let div_text = document.createElement('div');
  div_text.id = "text"; // アニメーション処理で対象の指定に必要なidを設定
  count++;
  div_text.classList.add('flowing-text'); // Googleフォントのクラスを追加
  div_text.style.position = 'fixed'; // テキストの位置を絶対位置にするための設定
  div_text.style.whiteSpace = 'nowrap'; // 画面右端での折り返しがなく、画面外へはみ出すようにする
  div_text.style.left = (document.documentElement.clientWidth) + 'px'; // 初期状態の横方向の位置は画面の右端に設定

  // フォントサイズの指定 (例: 300px)
  div_text.style.fontSize = '300px'; // ここでフォントサイズを指定
  
  // 画面中央に縦方向の位置を設定
  div_text.style.top = '50%'; // 画面の縦方向の中央に設定
  div_text.style.transform = 'translateY(-50%)'; // 中央に配置するために50%上にシフト

  div_text.appendChild(document.createTextNode('Genau')); // 画面上に表示されるテキストを設定
  document.body.appendChild(div_text); // body直下へ挿入

   // ライブラリを用いたテキスト移動のアニメーション： durationはアニメーションの時間、
   // 横方向の移動距離は「画面の横幅＋画面を流れるテキストの要素の横幅」、移動中に次の削除処理がされないようawait
  await gsap.to("#" + div_text.id, {duration: 5, x: -1 * (document.documentElement.clientWidth + div_text.clientWidth)});

  div_text.parentNode.removeChild(div_text); // 画面上の移動終了後に削除
}
</script>
</body>
</html>
