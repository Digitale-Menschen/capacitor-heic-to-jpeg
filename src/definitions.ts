export interface CapacitorHeicToJpegPlugin {
  /**
   * Provide the path (either an absolute path or an Android content URI) pointing to the HEIC/HEIF image you'd like to convert.
   * If you provide an Android content URI, the plugin will also return an Android content URI in exchange.
   *
   * @param options
   */
  convertToJpeg(options: { path: string }): Promise<{ path: string }>;
}
