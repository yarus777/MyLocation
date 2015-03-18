using System.Web.Mvc;
using Newtonsoft.Json;

namespace MyLocationServer.Controllers {
    public abstract class PostJsonController : Controller {
        protected T GetEventParameter<T>() {
            var json = Request["event"];
            return JsonConvert.DeserializeObject<T>(json);
        }
    }
}
