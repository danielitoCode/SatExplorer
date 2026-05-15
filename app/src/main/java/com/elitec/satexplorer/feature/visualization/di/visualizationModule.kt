package com.elitec.satexplorer.feature.visualization.di

import com.elitec.satexplorer.feature.visualization.data.renderEngine.OpenGlRendererEngine
import com.elitec.satexplorer.feature.visualization.domain.caseuse.BuildSceneGraphUseCase
import com.elitec.satexplorer.feature.visualization.domain.caseuse.GenerateRenderCommandsUseCase
import com.elitec.satexplorer.feature.visualization.presentation.renderer.GlSurfaceRenderer
import com.elitec.satexplorer.feature.visualization.presentation.util.RenderStateHolder
import com.elitec.satexplorer.feature.visualization.presentation.viewmodel.VisualizationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val visualizationModule = module {

    // STATE BRIDGE
    single { RenderStateHolder() }

    // OPENGL ENGINE
    single { OpenGlRendererEngine(get()) }

    // USE CASES
    factory { BuildSceneGraphUseCase() }

    factory { GenerateRenderCommandsUseCase() }

    // RENDERER
    factory {
        GlSurfaceRenderer(
            engine = get(),
            generateCommands = get(),
            stateHolder = get()
        )
    }

    // VIEWMODEL
    viewModel {
        VisualizationViewModel(
            buildSceneGraph = get(),
            stateHolder = get()
        )
    }
}