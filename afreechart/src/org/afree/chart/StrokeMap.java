/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * --------------
 * StrokeMap.java
 * --------------
 * 
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  shiraki  (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   Sato Yoshiaki ;
 *                   Niwano Masayoshi;
 *
 * Changes (from 19-Nov-2010)
 * --------------------------
 * 19-Nov-2010 : port JFreeChart 1.0.13 to Android as "AFreeChart"
 * 
 * ------------- JFreeChart ---------------------------------------------
 * (C) Copyright 2006-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes:
 * --------
 * 27-Sep-2006 : Version 1 (DG);
 *
 */

package org.afree.chart;


import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;


/**
 * A storage structure that maps <code>Comparable</code> instances with
 * <code>Stroke</code> instances.
 * <br><br>
 * To support cloning and serialization, you should only use keys that are
 * cloneable and serializable.  Special handling for the <code>Stroke</code>
 * instances is included in this class.
 *
 * @since JFreeChart 1.0.3
 */
public class StrokeMap implements Cloneable, Serializable {

    /** For serialization. */
    static final long serialVersionUID = -8148916785963525169L;

    /** Storage for the keys and values. */
    private transient Map store;

    /**
     * Creates a new (empty) map.
     */
    public StrokeMap() {
        this.store = new TreeMap();
    }

    /**
     * Returns the stroke associated with the specified key, or
     * <code>null</code>.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The stroke, or <code>null</code>.
     *
     * @throws IllegalArgumentException if <code>key</code> is
     *     <code>null</code>.
     */
    public Float getStroke(Comparable key) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        return (Float) this.store.get(key);
    }

    /**
     * Returns <code>true</code> if the map contains the specified key, and
     * <code>false</code> otherwise.
     *
     * @param key  the key.
     *
     * @return <code>true</code> if the map contains the specified key, and
     * <code>false</code> otherwise.
     */
    public boolean containsKey(Comparable key) {
        return this.store.containsKey(key);
    }

    /**
     * Adds a mapping between the specified <code>key</code> and
     * <code>stroke</code> values.
     *
     * @param key  the key (<code>null</code> not permitted).
     * @param stroke  the stroke.
     */
    public void put(Comparable key, Float stroke) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        this.store.put(key, stroke);
    }

    /**
     * Resets the map to empty.
     */
    public void clear() {
        this.store.clear();
    }

   

}
