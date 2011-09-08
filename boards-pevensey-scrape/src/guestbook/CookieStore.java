package guestbook;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CookieStore  {
    private Map<URI, ArrayList<HttpCookie>> storeMap = new HashMap<URI, ArrayList<HttpCookie>>();
 
    public void add(URI uri, HttpCookie cookie) {
        if (uri == null || cookie == null) {
            throw new NullPointerException();
        }
        ArrayList<HttpCookie> cookies;
        if (storeMap.containsKey(uri)) {
            cookies = storeMap.get(uri);
            cookies.remove(cookie);
            cookies.add(cookie);
        } else {
            cookies = new ArrayList<HttpCookie>();
            cookies.add(cookie);
            storeMap.put(uri, cookies);
        }
    }
 
    public List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException(); //$NON-NLS-1$
        }
        // get cookies associated with given URI. If none, returns an empty list
        List<HttpCookie> cookies = storeMap.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
        } else {
            // eliminate expired cookies
            for (HttpCookie cookie : cookies) {
                if (cookie.hasExpired()) {
                    cookies.remove(cookie);
                }
            }
        }
 
        // get cookies whose domain matches the given URI
        Set<URI> uris = storeMap.keySet();
        for (URI u : uris) {
            // exclude the given URI
            if (!u.equals(uri)) {
                List<HttpCookie> listCookie = storeMap.get(u);
                for (HttpCookie cookie : listCookie) {
                    if (HttpCookie.domainMatches(cookie.getDomain(), uri
                            .getHost())) {
                        if (cookie.hasExpired()) {
                            listCookie.remove(cookie);
                        } else if (!(cookie.hasExpired() || cookies
                                .contains(cookie))) {
                            cookies.add(cookie);
                        }
                    }
                }
            }
        }
 
        return cookies;
    }
 
    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        Collection<ArrayList<HttpCookie>> values = storeMap.values();
        for (ArrayList<HttpCookie> list : values) {
            for (HttpCookie cookie : list) {
                if (cookie.hasExpired()) {
                    list.remove(cookie); // eliminate expired cookies
                } else if (!cookies.contains(cookie)) {
                    cookies.add(cookie);
                }
            }
        }
        return Collections.unmodifiableList(cookies);
    }
 
    public List<URI> getURIs() {
        return new ArrayList<URI>(storeMap.keySet());
    }
 
    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException(); //$NON-NLS-1$
        }
        boolean success = false;
        Collection<ArrayList<HttpCookie>> values = storeMap.values();
        for (ArrayList<HttpCookie> list : values) {
            if (list.remove(cookie)) {
                success = true;
            }
        }
        return success;
    }
 
    public boolean removeAll() {
        if (!storeMap.isEmpty()) {
            storeMap.clear();
        }
        return true;
    }
}

