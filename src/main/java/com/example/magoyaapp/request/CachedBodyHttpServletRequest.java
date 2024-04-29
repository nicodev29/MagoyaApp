package com.example.magoyaapp.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] cachedBody;

    /*
     * Este constructor recibe una instancia de HttpServletRequest y lee todo el cuerpo de la solicitud
     * en un arreglo de bytes (cachedBody). Esto permite acceder al cuerpo de la solicitud múltiples veces,
     * ya que normalmente el cuerpo solo se puede leer una vez.
     */
    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException, IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = requestInputStream.readAllBytes();
    }

    /*
     * Este método sobrescribe el método getInputStream() de HttpServletRequest y devuelve una instancia
     * de CachedBodyServletInputStream que envuelve el arreglo de bytes cachedBody. Esto permite que
     * el cuerpo de la solicitud se pueda leer múltiples veces a partir del arreglo en caché.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    /*
     * Este método sobrescribe el método getReader() de HttpServletRequest y devuelve un BufferedReader
     * que lee del arreglo de bytes cachedBody. Esto también permite acceder al cuerpo de la solicitud
     * múltiples veces a partir del arreglo en caché.
     */
    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

}
