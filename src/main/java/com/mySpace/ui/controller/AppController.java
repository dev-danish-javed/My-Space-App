package com.mySpace.ui.controller;

import com.mySpace.service.AppService;
import com.mySpace.shared.constants.urlConstants.ControllerPaths;
import com.mySpace.shared.constants.urlConstants.actionPaths.AppPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerPaths.APP_CONTROLLER_PATH)
public class AppController {
    @Autowired
    AppService appService;
    @GetMapping(AppPaths.APP_SHUTDOWN_PATH)
    public void shutdownApp(){
        appService.shutdownApp();
        System.exit(0);
    }
}
