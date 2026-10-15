<?php
// onboarding.php
require_once __DIR__ . '/config/database.php';

$token = trim($_GET['token'] ?? '');$onboarding = null;
$mensajeEstado = null;
$tipoEstado = null; // 'completado' o 'error'

if (!$token) {
    $mensajeEstado = 'Enlace de onboarding inv&aacute;lido o no proporcionado.';$tipoEstado = 'error';
} else {
    try {
        $db = getDBConnection();
        $stmt =$db->prepare("SELECT * FROM onboarding WHERE token_unico = :token LIMIT 1");
        $stmt->execute([':token' =>$token]);
        $onboarding =$stmt->fetch(PDO::FETCH_ASSOC);

        if (!$onboarding) {
            $mensajeEstado = 'El enlace de onboarding proporcionado no existe o caduc&oacute;.';$tipoEstado = 'error';
        } else if ($onboarding['completado']) {$mensajeEstado = 'Este formulario ya fue completado previamente. Tu proyecto ya se encuentra en proceso de construcci&oacute;n.';
            $tipoEstado = 'completado';
        }
    } catch (Exception $e) {$mensajeEstado = 'Error de conexi&oacute;n a la base de datos: ' . htmlspecialchars($e->getMessage());$tipoEstado = 'error';
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Onboarding de tu Proyecto - WebParaTodos</title>
    
    <!-- Carga de la fuente Poppins -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Tailwind CSS y Configuración -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
      tailwind.config = {
        theme: {
          extend: {
            fontFamily: {
              sans: ['Poppins', 'sans-serif'],
            },
          },
        },
      }
    </script>
    <style>
      input, textarea, select {
        font-size: 16px !important;
      }
    </style>
</head>
<body class="bg-gray-50 font-sans min-h-screen py-8 px-4 flex items-center justify-center">

    <div class="max-w-xl w-full mx-auto">
        
        <!-- PANTALLA DE COMPLETADO O ERROR -->
        <?php if ($tipoEstado !== null): ?>
            <div class="bg-white p-8 sm:p-10 rounded-2xl shadow-sm border border-gray-100 text-center flex flex-col items-center justify-center">
                <img src="assets/webparatodos-logotipo-horizontal.svg" alt="WebParaTodos" class="h-10 mx-auto object-contain mb-6">

                <?php if ($tipoEstado === 'completado'): ?>
                    <div class="w-16 h-16 bg-blue-100 text-[#4158fc] rounded-full flex items-center justify-center mb-4 text-3xl">
                        🚀
                    </div>
                    <h1 class="text-xl sm:text-2xl font-bold text-gray-800 mb-2 text-center">&iexcl;Tu proyecto ya est&aacute; en marcha!</h1>
                    <p class="text-sm text-gray-600 mb-6 leading-relaxed max-w-md mx-auto text-center">
                        Recibimos exitosamente la informaci&oacute;n de 
                        <strong class="text-gray-800"><?= htmlspecialchars($onboarding['nombre_empresa'] ?? 'tu negocio') ?></strong>.
                        Nuestro equipo ya se encuentra construyendo tu nueva Landing Page.
                    </p>

                    <div class="bg-blue-50/60 border border-blue-100 rounded-xl p-4 text-center text-xs text-blue-900 space-y-2 mb-6 max-w-md mx-auto w-full">
                        <div class="flex items-center justify-center font-semibold text-sm text-[#4158fc] mb-1">
                            <span class="mr-2"></span> Plazo estimado de entrega: 5 d&iacute;as h&aacute;biles
                        </div>
                        <p class="text-center">Te notificaremos v&iacute;a WhatsApp tan pronto como la primera versi&oacute;n est&eacute; lista para tu revisi&oacute;n.</p>
                    </div>

                    <a href="index.php" class="inline-block w-full sm:w-auto bg-[#4158fc] hover:bg-[#3245d4] text-white px-8 py-3 rounded-xl text-sm font-semibold transition shadow-md text-center">
                        Ir al Panel de Cliente
                    </a>

                <?php else: ?>
                    <div class="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center mb-4 text-2xl font-bold">
                        &#10007;
                    </div>
                    <h1 class="text-xl font-bold text-gray-800 mb-2 text-center">Acceso No Disponible</h1>
                    <p class="text-sm text-gray-600 mb-6 text-center max-w-md mx-auto">
                        <?= $mensajeEstado ?>
                    </p>
                    <a href="index.php" class="inline-block bg-gray-800 hover:bg-gray-900 text-white px-6 py-2.5 rounded-xl text-sm font-semibold transition text-center">
                        Volver al Inicio
                    </a>
                <?php endif; ?>
            </div>

        <!-- FORMULARIO NORMAL DE ONBOARDING -->
        <?php else: ?>

            <!-- Logo Header -->
            <div class="text-center mb-6 sm:mb-8">
                <img src="assets/webparatodos-logotipo-horizontal.svg" alt="WebParaTodos" class="h-10 sm:h-12 mx-auto object-contain mb-3">
                <h1 class="text-xl sm:text-2xl font-bold text-gray-800">&iexcl;Demos vida a tu Landing Page!</h1>
                <p class="text-xs sm:text-sm text-gray-500 mt-1">Completa la informaci&oacute;n de tu negocio para iniciar la construcci&oacute;n.</p>
            </div>

            <!-- Indicador de Pasos -->
            <div class="mb-6 sm:mb-8 px-1">
                <div class="flex justify-between items-center mb-2 text-[11px] sm:text-xs font-semibold uppercase text-gray-500 gap-1">
                    <span id="step-label-1" class="text-[#4158fc] font-bold truncate">1. Datos</span>
                    <span id="step-label-2" class="truncate">2. Estrategia</span>
                    <span id="step-label-3" class="truncate">3. Conversi&oacute;n</span>
                    <span id="step-label-4" class="truncate">4. Dise&ntilde;o</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2 overflow-hidden">
                    <div id="progress-bar" class="bg-[#4158fc] h-2 w-1/4 transition-all duration-300"></div>
                </div>
            </div>

            <!-- Alerta de Error JS -->
            <div id="error-banner" class="hidden mb-4 p-4 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm"></div>

            <!-- Formulario Wizard -->
            <form id="onboarding-form" action="save_onboarding.php" method="POST" enctype="multipart/form-data" class="bg-white p-5 sm:p-8 rounded-2xl shadow-sm border border-gray-100">
                <input type="hidden" name="token" value="<?= htmlspecialchars($token) ?>">

                <!-- PASO 1: DATOS BÁSICOS -->
                <div id="step-1" class="wizard-step space-y-4 sm:space-y-5">
                    <h2 class="text-base sm:text-lg font-bold text-gray-800 border-b pb-2">Paso 1: Datos B&aacute;sicos de la Empresa</h2>
                    
                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Nombre Comercial de la Empresa *</label>
                        <input type="text" name="nombre_empresa" required placeholder="Ej: Clínica Dental Sonrisas" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300">
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Dominio Web Deseado o Existente *</label>
                        <input type="text" name="dominio_nic" required placeholder="Ej: dentalsonrisas.cl" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300">
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Ubicaci&oacute;n / Cobertura de Atenci&oacute;n *</label>
                        <input type="text" name="ubicacion_cobertura" required placeholder="Ej: Santiago Centro y Providencia (O Atención a todo Chile)" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300">
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Redes Sociales (Instagram, Facebook, LinkedIn - Opcional)</label>
                        <input type="text" name="redes_sociales[]" placeholder="https://instagram.com/tu_cuenta" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 mb-2">
                        <input type="text" name="redes_sociales[]" placeholder="https://facebook.com/tu_pagina" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300">
                    </div>
                </div>

                <!-- PASO 2: ESTRATEGIA -->
                <div id="step-2" class="wizard-step hidden space-y-4 sm:space-y-5">
                    <h2 class="text-base sm:text-lg font-bold text-gray-800 border-b pb-2">Paso 2: Estrategia y P&uacute;blico Objetivo</h2>
                    
                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Producto o Servicio Principal a Promocionar *</label>
                        <textarea name="producto_servicio" required rows="4" placeholder="Describe detalladamente tu oferta o servicio principal..." class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">&iquest;Qui&eacute;n es tu P&uacute;blico Objetivo? *</label>
                        <textarea name="publico_objetivo" required rows="4" placeholder="Ej: Hombres y mujeres de 25 a 50 años que buscan ortodoncia invisible en Santiago..." class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Promesa Principal / Resultado Final *</label>
                        <textarea name="promesa_principal" required rows="3" placeholder="Ej: Consigue la sonrisa perfecta que siempre soñaste en menos de 6 meses sin dolor." class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">&iquest;Qu&eacute; problema o dolor principal resuelves a tu cliente? *</label>
                        <textarea name="problema_cliente" required rows="4" placeholder="Ej: Inseguridad al sonreír en reuniones, falta de tiempo para citas extensas y precios elevados..." class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>
                </div>

                <!-- PASO 3: CONVERSIÓN -->
                <div id="step-3" class="wizard-step hidden space-y-4 sm:space-y-5">
                    <h2 class="text-base sm:text-lg font-bold text-gray-800 border-b pb-2">Paso 3: Elementos de Conversi&oacute;n</h2>
                    
                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">3 Beneficios Clave *</label>
                        <input type="text" name="beneficios_clave[]" required placeholder="Beneficio 1 (Ej: Atención 100% personalizada)" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 mb-2">
                        <input type="text" name="beneficios_clave[]" required placeholder="Beneficio 2 (Ej: Tecnología digital sin dolor)" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 mb-2">
                        <input type="text" name="beneficios_clave[]" required placeholder="Beneficio 3 (Ej: Facilidad de pago hasta en 12 cuotas)" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300">
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Proceso de Atenci&oacute;n / 3 Pasos *</label>
                        <textarea name="proceso_pasos" required rows="4" placeholder="Ej: &#10;1. Agendas tu evaluación gratuita&#10;2. Realizamos diagnóstico 3D&#10;3. Inicias tu tratamiento" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Prueba Social / Testimonios (Texto o Rese&ntilde;as de Google)</label>
                        <textarea name="prueba_social" rows="4" placeholder="Copia aquí 1 o 2 comentarios reales de tus clientes satisfechos..." class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Garant&iacute;a o Beneficio Extra de Cierre</label>
                        <textarea name="garantia" rows="2" placeholder="Ej: Evaluación y scanner inicial totalmente gratis con tu reserva." class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300 leading-relaxed"></textarea>
                    </div>
                </div>

                <!-- PASO 4: DISEÑO Y ARCHIVOS -->
                <div id="step-4" class="wizard-step hidden space-y-4 sm:space-y-5">
                    <h2 class="text-base sm:text-lg font-bold text-gray-800 border-b pb-2">Paso 4: Material Gr&aacute;fico y Estilo</h2>
                    
                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Subir Logotipo (PNG o SVG sin fondo preferentemente) *</label>
                        <input type="file" name="logo" required accept="image/*" class="w-full px-3 py-2.5 border border-gray-300 rounded-xl text-xs sm:text-sm bg-gray-50 focus:outline-none">
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Colores Corporativos Preferidos</label>
                        <input type="text" name="colores_corporativos" placeholder="Ej: Azul Marino (#003366) y Blanco" class="w-full px-3.5 py-2.5 border rounded-xl focus:ring-2 focus:ring-[#4158fc] outline-none border-gray-300">
                    </div>

                    <div>
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-2">&iquest;Qu&eacute; fotograf&iacute;as utilizaremos en tu sitio web? *</label>
                        <div class="space-y-2.5">
                            <label class="flex items-start space-x-3 text-xs sm:text-sm text-gray-700 cursor-pointer p-2.5 rounded-xl border border-gray-200 bg-gray-50">
                                <input type="radio" name="fotos_tipo" value="banco" checked onclick="toggleFotosUpload(false)" class="mt-0.5 text-[#4158fc] focus:ring-[#4158fc]">
                                <span>Banco de Im&aacute;genes Profesionales (Nosotros las seleccionamos por ti)</span>
                            </label>
                            <label class="flex items-start space-x-3 text-xs sm:text-sm text-gray-700 cursor-pointer p-2.5 rounded-xl border border-gray-200 bg-gray-50">
                                <input type="radio" name="fotos_tipo" value="propias" onclick="toggleFotosUpload(true)" class="mt-0.5 text-[#4158fc] focus:ring-[#4158fc]">
                                <span>Subir&eacute; mis propias fotograf&iacute;as de alta calidad</span>
                            </label>
                        </div>
                    </div>

                    <div id="fotos-upload-container" class="hidden">
                        <label class="block text-xs sm:text-sm font-medium text-gray-700 mb-1">Subir Im&aacute;genes Propias (Puedes seleccionar varias)</label>
                        <input type="file" name="fotos_propias[]" multiple accept="image/*" class="w-full px-3 py-2.5 border border-gray-300 rounded-xl text-xs sm:text-sm bg-gray-50 focus:outline-none">
                    </div>
                </div>

                <!-- Botones de Navegación -->
                <div class="flex justify-between items-center mt-8 pt-4 border-t gap-3">
                    <button type="button" id="prev-btn" onclick="changeStep(-1)" class="w-1/2 sm:w-auto px-5 py-3 rounded-xl text-xs sm:text-sm font-semibold border border-gray-300 text-gray-600 hover:bg-gray-50 transition hidden text-center">
                        &larr; Atr&aacute;s
                    </button>
                    <div class="ml-auto w-full sm:w-auto flex justify-end">
                        <button type="button" id="next-btn" onclick="changeStep(1)" class="w-full sm:w-auto bg-[#4158fc] hover:bg-[#3245d4] text-white px-7 py-3 rounded-xl text-xs sm:text-sm font-semibold transition shadow-md text-center">
                            Siguiente &rarr;
                        </button>
                        <button type="submit" id="submit-btn" class="w-full sm:w-auto bg-green-600 hover:bg-green-700 text-white px-7 py-3 rounded-xl text-xs sm:text-sm font-semibold transition shadow-md hidden text-center">
                            Finalizar y Enviar
                        </button>
                    </div>
                </div>
            </form>

            <!-- Screen de Carga al Enviar -->
            <div id="loading-overlay" class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm hidden flex-col items-center justify-center text-white z-50 p-4 text-center">
                <div class="w-12 h-12 border-4 border-white/30 border-t-white rounded-full animate-spin mb-4"></div>
                <h3 class="text-lg font-bold">Enviando informaci&oacute;n...</h3>
                <p class="text-xs text-gray-200 mt-1 max-w-xs">Por favor no cierres esta ventana. Estamos guardando tu respuesta y procesando las im&aacute;genes.</p>
            </div>

            <script>
                let currentStep = 1;
                const totalSteps = 4;

                function utf8_to_b64(str) {
                    return window.btoa(unescape(encodeURIComponent(str)));
                }

                function updateWizardUI() {
                    document.querySelectorAll('.wizard-step').forEach((el, index) => {
                        el.classList.toggle('hidden', index + 1 !== currentStep);
                    });

                    const progressPercent = (currentStep / totalSteps) * 100;
                    document.getElementById('progress-bar').style.width = `${progressPercent}%`;

                    for (let i = 1; i <= totalSteps; i++) {
                        const label = document.getElementById(`step-label-${i}`);
                        if (i === currentStep) {
                            label.className = 'text-[#4158fc] font-bold truncate';
                        } else if (i < currentStep) {
                            label.className = 'text-green-600 font-medium truncate';
                        } else {
                            label.className = 'text-gray-400 font-normal truncate';
                        }
                    }

                    document.getElementById('prev-btn').classList.toggle('hidden', currentStep === 1);
                    document.getElementById('next-btn').classList.toggle('hidden', currentStep === totalSteps);
                    document.getElementById('submit-btn').classList.toggle('hidden', currentStep !== totalSteps);

                    window.scrollTo({ top: 0, behavior: 'smooth' });
                }

                function changeStep(delta) {
                    if (delta === 1) {
                        const currentStepContainer = document.getElementById(`step-${currentStep}`);
                        const requiredInputs = currentStepContainer.querySelectorAll('[required]');
                        let valid = true;

                        requiredInputs.forEach(input => {
                            if (!input.reportValidity()) {
                                valid = false;
                            }
                        });

                        if (!valid) return;
                    }

                    currentStep += delta;
                    if (currentStep < 1) currentStep = 1;
                    if (currentStep > totalSteps) currentStep = totalSteps;

                    updateWizardUI();
                }

                function toggleFotosUpload(show) {
                    const container = document.getElementById('fotos-upload-container');
                    if (show) {
                        container.classList.remove('hidden');
                    } else {
                        container.classList.add('hidden');
                    }
                }

                document.getElementById('onboarding-form').addEventListener('submit', async function(e) {
                    e.preventDefault();

                    const form = this;
                    const submitBtn = document.getElementById('submit-btn');
                    const overlay = document.getElementById('loading-overlay');
                    const errorBanner = document.getElementById('error-banner');

                    errorBanner.classList.add('hidden');
                    submitBtn.disabled = true;
                    overlay.classList.remove('hidden');
                    overlay.classList.add('flex');

                    try {
                        const formData = new FormData();
                        formData.append('token', form.querySelector('[name="token"]').value);

                        const elements = form.elements;
                        for (let i = 0; i < elements.length; i++) {
                            const el = elements[i];
                            if (!el.name || el.disabled) continue;

                            if (el.type === 'file') {
                                if (el.files.length > 0) {
                                    for (let j = 0; j < el.files.length; j++) {
                                        formData.append(el.name, el.files[j]);
                                    }
                                }
                            } else if (el.type === 'radio') {
                                if (el.checked) {
                                    formData.append(el.name, el.value);
                                }
                            } else if (el.type !== 'button' && el.type !== 'submit' && el.type !== 'hidden') {
                                const encodedVal = utf8_to_b64(el.value.trim());
                                formData.append(el.name, encodedVal);
                            }
                        }

                        const response = await fetch('save_onboarding.php', {
                            method: 'POST',
                            body: formData,
                            headers: {
                                'X-Requested-With': 'XMLHttpRequest',
                                'Accept': 'application/json'
                            }
                        });

                        const result = await response.json();

                        if (result.success) {
                            window.location.reload();
                        } else {
                            throw new Error(result.error || 'Ocurri&oacute; un error al procesar la informaci&oacute;n.');
                        }

                    } catch (err) {
                        overlay.classList.add('hidden');
                        overlay.classList.remove('flex');
                        submitBtn.disabled = false;

                        errorBanner.textContent = err.message || 'Error al conectar con el servidor. Por favor intenta de nuevo.';
                        errorBanner.classList.remove('hidden');
                        window.scrollTo({ top: 0, behavior: 'smooth' });
                    }
                });
            </script>
        <?php endif; ?>

    </div>

</body>
</html>