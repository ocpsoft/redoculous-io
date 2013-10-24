$(document).ready(function()
{
   $('.focus:first').focus();
});

$(document).ready(function($)
{

   $(".clickable").click(function()
   {
      var attr = $(this).attr("data-href");
      if (typeof attr !== 'undefined' && attr !== false)
      {
         window.document.location = attr;
      }
   });
   $(".clickable").each(function()
   {
      $(this).attr("data-bg", $(this).css('backgroundColor'));
      $(this).children().attr("data-bg", $(this).children().css('backgroundColor'));
   });
   $('.clickable').mouseover(function()
   {
      $(this).stop().animate({
         'backgroundColor' : '#FAFCB8'
      }, 200);
      $(this).children().stop().animate({
         'backgroundColor' : '#FAFCB8'
      }, 200);
   }).mouseout(function()
   {
      $(this).stop().animate({
         'backgroundColor' : $(this).attr("data-bg")
      }, 200);
      $(this).children().stop().animate({
         'backgroundColor' : $(this).children().attr("data-bg")
      }, 200);
   });
   $(".clickable").focus(function()
   {
      $(this).stop().animate({
         'backgroundColor' : '#FAFCB8'
      }, 200);
      $(this).children().stop().animate({
         'backgroundColor' : '#FAFCB8'
      }, 200);
      
	  $(this).attr("data-focus-bg", $(this).attr("data-bg"));
      $(this).attr("data-bg", '#FAFCB8');
   });
   $(".clickable").blur(function()
   {
	   $(this).attr("data-bg", $(this).attr("data-focus-bg"));
	   $(this).stop().animate({
		   'backgroundColor' : $(this).attr("data-bg")
	   }, 200);
	   $(this).children().stop().animate({
		   'backgroundColor' : $(this).attr("data-bg")
	   }, 200);
   });
});