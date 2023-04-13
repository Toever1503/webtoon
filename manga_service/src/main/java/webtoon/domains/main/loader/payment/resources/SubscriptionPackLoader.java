package webtoon.domains.main.loader.payment.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import webtoon.payment.resources.SubscriptionPackResources;

@Controller
@RequestMapping("/subscription_pack")
public class SubscriptionPackLoader extends SubscriptionPackResources {
}
