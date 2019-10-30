package com.demai.cornel.auth.service;

import java.util.List;

public interface IUrlAclService {

    List<String> getURLAcls(String url);

    boolean checkUserUrlAcls(String userId, String url);
}
