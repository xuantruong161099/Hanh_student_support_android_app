package com.meowbeos.studentsupport.model;

import java.util.List;

/**
 * Project name: StudentSupport
 * Created by MeowBeos
 * Date: 26/12/2020
 */

public class CollectNews {
    private NewsPins listNewsPins;
    private List<GeneralNews> listGeneralNews;

    public NewsPins getListNewsPins() {
        return listNewsPins;
    }

    public void setListNewsPins(NewsPins listNewsPins) {
        this.listNewsPins = listNewsPins;
    }

    public List<GeneralNews> getListGeneralNews() {
        return listGeneralNews;
    }

    public void setListGeneralNews(List<GeneralNews> listGeneralNews) {
        this.listGeneralNews = listGeneralNews;
    }
}
