
let loadMore = 0;
const NUM_RESULTS = 10;

async function initElements() {
  loadMore = 0
  let flag = false
  const RESPONSE = await fetch(`/starterNews?iteracion=${loadMore}`);
  let data = await RESPONSE.json();
  let news = data[0];
  const MAX = data[1];
  flag = MAX
  addElementsMainContainer(news);
  let moreNews = document.getElementById("container-loadMore");
  if (flag) {
    moreNews.style.display = "flex";
  }
  else {
    moreNews.style.display = "none";
  }
}

async function loadMoreFoo() {

  let flag = false
  loadMore = loadMore + 1

  let spinnerContainer = document.getElementById("spinner-container");
  let moreNews = document.getElementById("container-loadMore");
  spinnerContainer.style.display = "flex";
  moreNews.style.display = "none";
  const RESPONSE = await fetch(`/starterNews?iteracion=${loadMore}`);
  let data = await RESPONSE.json()
  let news = data[0];
  const MAX = data[1];
  flag = MAX;
  addElementsMainContainer(news)
  spinnerContainer.style.display = "none"
  // setTimeout(() => {spinnerContainer.style.display = "none"}, 2000); // To show that the spinner works correctly


  if (flag) {
    moreNews.style.display = "flex";
  }


}




function addElementsMainContainer(news) {

  let containerNews = document.getElementById("container-news");

  if (news.length == 0) {
    containerNews.innerText = "NO TIENES NINGUNA NOVEDAD"
    containerNews.style.textAlign = "center"; 
  containerNews.style.color = "white"; 
  containerNews.style.fontSize = "30px"; 
  }

  for (let i = 0; i < (news.length); i += 2) {
    let row = document.createElement('div');
    row.classList.add('row', 'mb-3');

    for (let j = 0; j < 2; j++) {
      if (i + j < news.length) {
        let col = document.createElement('div');
        col.classList.add('col-md-6');

        let card = document.createElement('div');
        card.classList.add('card', 'mb-3');

        let cardBody = document.createElement('div');
        cardBody.classList.add('card-body');

        let cardTitle = document.createElement('h5');
        cardTitle.classList.add('card-title');
        cardTitle.textContent = `NOVEDAD`;

        let cardText = document.createElement('p');
        cardText.classList.add('card-text');

        let link = document.createElement('a');
        link.href = `/mainPage/showRutine?id=${news[i + j].rutine.id}`;

        link.textContent = `Nueva rutina de: ${news[i + j].alias}`;

        link.style.textDecoration = 'none';

        cardText.append(link);


        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);

        card.appendChild(cardBody);

        col.appendChild(card);

        row.appendChild(col);
      }
    }

    containerNews.appendChild(row);

  }

}


initElements();







