package io.github.codecraft87.eshop.security.config;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

  private JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
//    http.csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(
//            auth -> auth.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
//        .sessionManagement(
//            sessionMgr -> sessionMgr.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .httpBasic(Customizer.withDefaults())
//        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//    return http.build();
//  }

//  @Bean
//  public AuthenticationProvider authenticationProvider(
//      UserServiceImpl userService, BCryptPasswordEncoder encoder) {
//
//    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
//    provider.setPasswordEncoder(encoder);
//    return provider;
//  }

//  @Bean
//  public BCryptPasswordEncoder encoder() {
//    return new BCryptPasswordEncoder();
//  }
//
////  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
//
//    return configuration.getAuthenticationManager();
//  }
}
