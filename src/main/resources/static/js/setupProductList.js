let hiddenElements = document.getElementsByClassName("productDiv");
let size = hiddenElements.length;
let indexToDisplay = []

for(let i = 0; i < size; i++) {
    let select = hiddenElements[i].querySelector("select");
    if(select.options[select.selectedIndex].text != "Produkt"){
        hiddenElements[i].classList.remove("hidden");
    }
}