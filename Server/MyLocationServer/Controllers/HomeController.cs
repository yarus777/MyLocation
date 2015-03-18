using System.Web.Mvc;

namespace MyLocationServer.Controllers {
    public class HomeController : Controller {
        public ActionResult Index() {
            return Json("Hello", JsonRequestBehavior.AllowGet);
        }
    }
}
