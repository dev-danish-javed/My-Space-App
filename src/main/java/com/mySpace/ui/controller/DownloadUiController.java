package com.mySpace.ui.controller;

import com.mySpace.shared.constants.urlConstants.ControllerPaths;
import com.mySpace.ui.model.response.DownloadLinkDataViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(ControllerPaths.DOWNLOAD)
public class DownloadUiController {
    @Autowired
    Map<String, DownloadLinkDataViewModel> downloadDataBean;

    @GetMapping("{id}")
    public ModelAndView download(@PathVariable String id){
        ModelAndView downloadUi = new ModelAndView("download-ui");
        downloadUi.addObject("downloadData", downloadDataBean.get(id));
        return downloadUi;
    }
}
