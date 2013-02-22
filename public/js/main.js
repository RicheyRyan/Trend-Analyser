var trends;
var tweets;

var sidebar = $(".trend-sidebar");
var tweetHolder = $(".tweet-holder");

$(function() {

    function loadSideBar(list){
        sidebar.empty();
        sidebar.append('<li class="nav-header">Trends</li>');
        $(list).each(function(index, item){
            sidebar.append('<li><a href="#">' + item + '</a></li>');
        });
    }

    function loadTweets(list){
        //tweetHolder.empty();
        $(list).each(function(index, item){
           //tweetHolder.append('<li><blockquote class="twitter-tweet"> <a href="https://twitter.com/twitterapi/status/' + item + '"></a></blockquote></li>');
           tweetHolder.append('<li><blockquote class="twitter-tweet"> <a href="https://twitter.com/twitterapi/status/' + item + '"></a></blockquote></li>');
            //tweetHolder.append('<li>hi</li>');
            //$.getScript('//playform.twitter.com/widgets.js')

            //twttr.widgets.load();
        });

    }

    function getTrends(){
        var url = "http://localhost:3000/trends";
        $.getJSON(url, function(data){
            trends = data;
            loadSideBar(trends);
        }); 
    }

    function getTweets(){
        var url = "http://localhost:3000/tweets/techVpoverty";
        $.getJSON(url, function(data){
            tweets = data;
            loadTweets(tweets);
            //console.log(tweets);
        }); 
    }

    // Pull contacts from the server
    getTrends();
    getTweets();
});


