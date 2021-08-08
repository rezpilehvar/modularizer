package ir.irezaa.modularer.di

import dagger.Component
import javax.inject.Named

@Component(dependencies = [RetrofitComponent::class])
interface ApiComponent {
}