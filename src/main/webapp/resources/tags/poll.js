if (!window.redoculoustags)
{
   var redoculoustags = {};
}

if (!redoculoustags.poll)
{
   redoculoustags.poll = {};
}

if (!redoculoustags.poll.init)
{
   redoculoustags.poll.init = function init(id, inc, to, rend, canc)
   {
      var componentID = id;
      var increment = inc;
      var timeout = to;
      var elapsed = 0;
      var token = null;
      var render = rend;
      var cancel = canc;

      if (typeof cancel == 'undefined' && cancel != "")
      {
         cancel = false;
      }

      // If increment isn't set, or it's less than some reasonable value (50)
      // default to 200ms
      if (isNaN(increment) || increment <= 50)
      {
         increment = 200;
      }

      // If timeout isn't set, default to no timeout
      if (isNaN(timeout) || timeout == 0)
      {
         timeout = -1;
      }

      var poll = function poll()
      {
         // Not an accurate timeout - but simple to compute
         elapsed += Number(increment);
         if (eval(cancel))
         {
            window.clearInterval(token);
         }
         else if (timeout != -1 && elapsed > timeout)
         {
            window.clearInterval(token);
         }
         else
         {
            jsf.ajax.request(componentID, null, {
               render : render
            });
         }
      };

      token = window.setInterval(poll, increment);

      return function cancelPoll(data)
      {
         if (data.source.id == componentID)
         {
            window.clearInterval(token);
         }
      };
   };
}