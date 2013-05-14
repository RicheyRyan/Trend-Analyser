$(function() {

    var trends = [];
    var tweets = [];

    var sidebar = $(".trend-sidebar");
    var tweetHolder = $(".tweet-holder");
    var hashtagBar = $(".hashtags");

    function loadSideBar(list){
        sidebar.empty().append('<li class="nav-header">Trends</li>');
        $(list).each(function(index, item){
            sidebar.append('<li class="trend"><a href="#" >' + item + '</a></li>');
        });

        //$("active").removeClass("active");

        $(".trend").click( function(){
            $(".trend").filter( function (index){
                return $(this).hasClass("active");
            }).removeClass("active");
            var text = $(this).text();
            //console.log($(this));
            $(this).addClass("active");
            getTweets(text);
            getHashtags(text);
        });

        $($(".trend")[0]).addClass("active");

    }

    function loadTweets(list){
        tweetHolder.empty();
        $(list).each(function(index, item){
           tweetHolder.append('<li><blockquote class="twitter-tweet"> <a href="https://twitter.com/twitterapi/status/' + item + '"></a></blockquote></li>');
           //console.log(index);
           twttr.widgets.load();
        });
        //console.log(tweetHolder.html());
        twttr.widgets.load();
    }

    function loadHashtags(list){
        hashtagBar.empty();
        $(list).each(function(index, item){

            //hashtagBar.append('<p><a href="https://twitter.com/search?q='+item+'">' + item + '</a></p>');
            hashtagBar.append('<a href="https://twitter.com/search?q='+item+'" class="badge badge-info">'+ item + '</a>');
        });
    }

    function getTrends(){
        var url = "http://localhost:3000/trends";
        $.getJSON(url, function(data){
            trends = data;
            loadSideBar(trends);
            var firstTrend = trends[0];
            getTweets(firstTrend);
            getHashtags(firstTrend);
        }); 
    }

    function getTweets(query){
        var url = "http://localhost:3000/tweets/" + encodeURIComponent(query);
        $.getJSON(url, function(data){
            tweets = data;
            loadTweets(tweets);
            //console.log(tweets);
        }); 
    }

    function getHashtags(query){
        var url = "http://localhost:3000/hashtags/" + encodeURIComponent(query);
        $.getJSON(url, function(data){
            hashtags = data;
            loadHashtags(hashtags);
            //console.log(hashtags);
        }); 
    }

    


    // Pull trends from the server
    getTrends();
});


