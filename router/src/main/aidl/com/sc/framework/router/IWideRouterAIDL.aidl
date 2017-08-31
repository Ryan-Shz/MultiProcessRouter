// IWideRouter.aidl
package com.sc.framework.router;

import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;

// Declare any non-default types here with import statements

interface IWideRouterAIDL {

    RouterResponse route(in RouterRequest routerRequest);

}
