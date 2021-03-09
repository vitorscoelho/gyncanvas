package vitorscoelho.gyncanvas.webgl

/*
RESUMO DOS PASSOS DE SETUP E DESENHO
At Init time
    1) create all shaders and programs and look up locations
    2) create buffers and upload vertex data
    3) create textures and upload texture data

At Render Time
    1) clear and set the viewport and other global state (enable depth testing, turn on culling, etc..)
    2) For each thing you want to draw
        2.1) call gl.useProgram for the program needed to draw.
        2.2) setup attributes for the thing you want to draw
                -for each attribute call gl.bindBuffer, gl.vertexAttribPointer, gl.enableVertexAttribArray
        2.3) setup uniforms for the thing you want to draw
                -call gl.uniformXXX for each uniform
                -call gl.activeTexture and gl.bindTexture to assign textures to texture units.
        2.4) call gl.drawArrays or gl.drawElements

 */