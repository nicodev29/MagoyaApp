package com.example.magoyaapp.request;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyServletInputStream extends ServletInputStream {
    private final InputStream cachedBodyInputStream;

    /*
     * Este constructor recibe un arreglo de bytes y crea un ByteArrayInputStream a partir de él.
     * Este InputStream se utiliza para leer el cuerpo de la solicitud en caché.
     */
    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    /*
     * Este método verifica si se ha leído todo el cuerpo de la solicitud en caché.
     * Devuelve true si no hay más bytes por leer (available() == 0), false en caso contrario.
     */
    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Este método siempre devuelve true, ya que el cuerpo de la solicitud está en caché y siempre está listo para ser leído.
     */
    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    /*
     * Este método lee un byte del cuerpo de la solicitud en caché y lo devuelve como un int.
     */
    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}
