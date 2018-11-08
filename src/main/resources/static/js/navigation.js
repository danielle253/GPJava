$(".menu-element").css("height", (100 /  $(".menu-element").length) + "%");

var xhr = new XMLHttpRequest();

function switchContent(content){
    $(".content-container").hide("#map");
    xhr.open('GET', content, true);

    xhr.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            $(".content-container").html(this.responseText);
            $(".content-container").show("#map");
        }
    }

    xhr.send();
}

function map(){
    switchContent('map');
}

function accounts(){
    switchContent('accounts');
}

function bookings(){}
function messages(){}
function settings(){}
function logs(){}

map(); //Default