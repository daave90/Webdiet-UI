let addProductButton = document.getElementById("addProduct");

addProductButton.addEventListener("click", (event) => {
    event.preventDefault();
    let hiddenElements = document.getElementsByClassName("hidden");
    if(hiddenElements.length < 0) {
        console.log("No more hidden elements");
    } else {
        hiddenElements[0].classList.remove("hidden");
    }
});