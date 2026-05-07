# 🚀 JFlutter

> **Un framework moderno para construir interfaces gráficas en Java**, inspirado en la filosofía de Flutter de Google

JFlutter es una biblioteca Java que simplifica el desarrollo de aplicaciones de escritorio con interfaz gráfica moderna. Adapta los conceptos innovadores de Flutter a Java, proporcionando un sistema de componentes jerárquico, reativo y fácil de usar.

---

## 📋 Tabla de Contenidos

- [Características Principales](#características-principales)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Componentes](#componentes)
- [Sistema de Eventos](#sistema-de-eventos)
- [Decoraciones](#decoraciones)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Autor](#autor)

---

## ✨ Características Principales

### 🎨 **Sistema de Componentes Jerárquico**
- Arquitectura basada en árbol de componentes, similar a Flutter
- Cada componente cuenta con su propio ciclo de vida, layout y renderizado
- Soporte completo para composición de componentes

### 📐 **Layout Automatizado**
- Sistema de layout reactivo que se ajusta automáticamente
- Invalidación inteligente de layouts para máximo rendimiento
- Soporte para alineación flexible: CENTER, TOP, BOTTOM, LEFT, RIGHT
- Contenedores de una sola línea (Row/Column) y layouts complejos (Stack, Flex)

### 🎯 **Sistema de Eventos Completo**
- **Eventos de Acción**: clics de ratón y acciones de usuario
- **Eventos de Hover**: detección automática de paso del ratón
- **Eventos de Teclado**: captura de entrada de teclado
- **Eventos de Rueda**: soporte para scroll
- **Sistema de Enfoque**: gestión centralizada de componentes enfocados

### 🖌️ **Decoraciones y Estilos**
- **Bordes personalizados**: grosor, color y estilo configurable
- **Sombras de caja**: efectos visuales profesionales
- **Fondos**: colores y gradientes
- **Sistema modular**: combina decoraciones de forma flexible

### ♿ **Validación y Detalles de Implementación**
- Validación de límites de componentes
- Detección de errores en fase de compilación
- Hit testing inteligente para eventos de usuario
- Clipping automático de hijos a límites del padre

---

## 🔧 Requisitos

- **Java 23** o superior
- **Maven 3.6+** (para compilación y gestión de dependencias)

### Dependencias
- `org.jetbrains:annotations` - Anotaciones para análisis estático de código
- `rroyo.jutils:jutils 1.0.3` - Utilidades propias del autor

---

## 📦 Instalación

### Con Maven (Recomendado)

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/rroyo6500/JFlutter.git
   cd JFlutter
   ```

2. **Compilar el proyecto:**
   ```bash
   mvn clean install
   ```

3. **Agregar a tu proyecto Maven:**
   ```xml
   <dependency>
       <groupId>rroyo.JF</groupId>
       <artifactId>jflutter</artifactId>
       <version>1.0.0-SNAPSHOT</version>
   </dependency>
   ```

4. **Configurar el repositorio en tu `pom.xml`:**
   ```xml
   <repositories>
       <repository>
           <id>github</id>
           <url>https://maven.pkg.github.com/rroyo6500/JFlutter</url>
       </repository>
   </repositories>
   ```

---

## 📁 Estructura del Proyecto

```
JFlutter/
├── src/main/java/rroyo/JF/
│   ├── JFComponents/           # Componentes UI
│   │   ├── BaseComponent/      # Clases base abstractas
│   │   ├── SimpleComponents/   # Componentes básicos (Text, Container, Image, etc)
│   │   └── ComplexComponents/  # Componentes interactivos (Button, CheckBox, TextField, etc)
│   ├── JFEvents/               # Sistema completo de eventos
│   ├── Enums/                  # Enumeraciones (Alignment, MainAxisAlignment, etc)
│   └── Decorations/            # Sistema de decoraciones (Border, BoxShadow, etc)
├── src/test/                   # Tests unitarios
├── pom.xml                     # Configuración Maven
└── README.md                   # Este archivo
```

---

## 🧩 Componentes

### Componentes Simples

| Componente | Descripción |
|-----------|------------|
| **JFText** | Renderiza texto con fuente, color y fondo configurables |
| **JFContainer** | Caja de tamaño fijo con decoración personalizada |
| **JFImage** | Carga y muestra imágenes en diferentes formatos |
| **JFCanvas** | Lienzo para dibujo customizado con Graphics2D |
| **JFSizedBox** | Caja con tamaño específico, útil para espaciadores |
| **JFCenter** | Centra su hijo en el espacio disponible |

### Contenedores de Layout

| Componente | Descripción |
|-----------|------------|
| **JFRow** | Dispone hijos horizontalmente, izquierda a derecha |
| **JFColumn** | Dispone hijos verticalmente, arriba a abajo |
| **JFStack** | Superpone hijos con control de alineación |
| **JFFlex** | Flexibilidad en distribución de tamaños |
| **JFViewport** | Área visible dentro de contenido más grande |
| **JFScrollColumn** | Columna con soporte para scroll vertical |

### Componentes Interactivos

| Componente | Descripción |
|-----------|------------|
| **JFButton** | Botón clickeable con estados visuales |
| **JFCheckBox** | Casilla de verificación (checkbox) |
| **JFRadioButton** | Botones de radio para selecciones únicas |
| **JFTextField** | Campo de entrada de texto |
| **JFComboBox** | Lista desplegable de opciones |
| **JFScrollBar** | Barra de desplazamiento personalizable |

### Componentes Especializados

| Componente | Descripción |
|-----------|------------|
| **JFWindow** | Ventana raíz - contenedor de nivel superior |
| **JFGroup** | Agrupa elementos sin impacto visual |
| **JFSizedStack** | Stack con validación de tamaño |
| **JFMargin** | Componente con márgenes internos |

---

## 🎪 Sistema de Eventos

JFlutter proporciona un sistema de eventos robusto y jerárquico:

### Tipos de Eventos Soportados

```java
// Evento de Acción (clicks de ratón)
component.addActionListener(event -> {
    System.out.println("¡Fue clickeado!");
});

// Evento de Hover (paso del ratón)
component.addHoverListener(event -> {
    if (event.isEntering()) {
        System.out.println("El ratón entró");
    }
});

// Evento de Teclado
component.addKeyListener(event -> {
    System.out.println("Tecla presionada: " + event.getKeyCode());
});

// Evento de Rueda (scroll)
component.addWheelListener(event -> {
    System.out.println("Rotación: " + event.getRotation());
});
```

### Características del Sistema de Eventos

- **Enrutamiento Automático**: Los eventos se propagan automáticamente al componente correcto
- **Gestor de Eventos**: `JFEventStore` centraliza y almacena todos los listeners
- **Tipos de Eventos**: ActionEventTypes, HoverEventTypes, KeyEventTypes
- **Hit Testing**: Detección inteligente del componente bajo el cursor

---

## 🖌️ Decoraciones

La decoración permite estilizar componentes de forma profesional:

### Decoración Simple

```java
// Solo color de fondo
Decoration decoration = new Decoration(Color.BLUE);
JFContainer container = new JFContainer(200, 100, decoration);
```

### Con Borde

```java
Decoration decoration = new Decoration(Color.WHITE);
Border border = new Border(2, Color.BLACK);
decoration.setBorder(border);
```

### Con Sombra

```java
Decoration decoration = new Decoration(Color.WHITE);
BoxShadow shadow = new BoxShadow(5, 5, 10, Color.GRAY);
decoration.setBoxShadow(shadow);
```

---

## 💻 Ejemplos de Uso

### Ejemplo 1: Ventana Simple con Texto

```java
import rroyo.JF.JFComponents.SimpleComponents.*;

import java.awt.*;

public class SimpleApp {
    public static void main(String[] args) {
        JFWindow window = new JFWindow(800, 600, "Mi Aplicación").setVisible(true);
        window.addChild(
                new JFContainer(300, 100, Color.LIGHT_GRAY).addChild(
                        new JFText("¡Hola, JFlutter!")
                                .setFont(new Font("Arial", Font.BOLD, 24))
                                .setColor(Color.BLUE)
                )
        );
    }
}
```

### Ejemplo 2: Layout con Row y Column

```java
// Crear un layout vertical con texto e imagen
JFColumn layoutVertical = new JFColumn();

JFText titulo = new JFText("Mi Aplicación");
JFImage imagen = new JFImage("logo.png");

layoutVertical.addChild(titulo);
layoutVertical.addChild(imagen);

window.addChild(layoutVertical);
```

### Ejemplo 3: Botón Interactivo

```java
JFButton boton = new JFButton("Clic aquí");

boton.addActionListener(event -> {
    System.out.println("¡Botón presionado!");
    // Tu lógica aquí
});

window.addChild(boton);
```

### Ejemplo 4: Layout Responsive con Stack

```java
// Crear un layout superpuesto
JFStack stack = new JFStack();

JFContainer fondo = new JFContainer(500, 500, Color.WHITE);
JFText portada = new JFText("Contenido Centrado");
portada.setFont(new Font("Arial", Font.BOLD, 18));

stack.addChild(fondo);
stack.addChild(portada);
stack.setAlignment(Alignment.CENTER);

window.addChild(stack);
```

---

## 🏗️ Arquitectura Interna

### Componentes Base

- **JFComponent**: Clase abstracta base para todos los componentes
  - Gestiona: árbol de componentes, geometría, layout, renderizado
  - Proporciona: hit testing, invalidación de layout, navegación de árbol

- **JFSingleChildComponent**: Interfaz para componentes con un único hijo

- **JFMultiChildComponent**: Interfaz para contenedores con múltiples hijos

### Ciclo de Vida

1. **Inicialización**: El componente se adjunta al árbol
2. **Layout**: Cálculo recursivo de geometría
3. **Validación**: Verificación de límites y restricciones
4. **Renderizado**: Dibujo en la ventana
5. **Eventos**: Captura y procesamiento de entrada del usuario

---

## 🚀 Características Avanzadas

### Validación Inteligente
- Los componentes validan automáticamente que no salgan de los límites del padre
- Detección temprana de errores de layout en fase de compilación

### Clipping Automático
```java
container.setClipChildrenToBounds(true);
// Los hijos se recortarán a los límites del contenedor
```

### Overflow Control
```java
component.setOverflowAllowed(true);
// El componente puede extenderse fuera de su padre
```

### Visibilidad e Inactividad
```java
component.setVisible(false);    // No se renderiza pero participa en layout
component.setActive(false);     // No participa en layout ni en eventos
```

---

## 📊 Alineación

El enum `Alignment` proporciona posiciones predefinidas para componentes:

```java
point = Alignment.CENTER.calculatePosition(box, childWidth, childHeight);
point = Alignment.TOP.calculatePosition(box, childWidth, childHeight);
point = Alignment.BOTTOM.calculatePosition(box, childWidth, childHeight);
point = Alignment.LEFT.calculatePosition(box, childWidth, childHeight);
point = Alignment.RIGHT.calculatePosition(box, childWidth, childHeight);
```

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para proponer cambios:

1. Realiza un fork del proyecto
2. Crea una rama para tu característica (`git checkout -b feature/AmazingFeature`)
3. Haz commit de tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Sube tu rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 👨‍💻 Autor

**Roberto Royo** 
- GitHub: [@rroyo6500](https://github.com/rroyo6500)
- Proyecto: [JFlutter](https://github.com/rroyo6500/JFlutter)


