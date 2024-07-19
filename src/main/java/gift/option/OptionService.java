package gift.option;

import static gift.exception.ErrorMessage.OPTION_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.OPTION_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;

import gift.product.Product;
import gift.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(
        OptionRepository optionRepository,
        ProductRepository productRepository
    ) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionDTO> getOptions(long productId) {
        isProductExists(productId);

        return optionRepository.findAllByProductId(productId)
            .stream()
            .map(option -> new OptionDTO(
                option.getId(),
                option.getName(),
                option.getQuantity()
            )).toList();
    }

    public void addOption(long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        if (optionRepository.existsById(optionDTO.getId())) {
            throw new IllegalArgumentException(OPTION_ALREADY_EXISTS);
        }

        isOptionNameExists(productId, optionDTO.getName());

        optionRepository.save(new Option(
            optionDTO.getId(),
            optionDTO.getName(),
            optionDTO.getQuantity(),
            product
        ));
    }

    public void updateOption(long productId, OptionDTO optionDTO) {
        isProductExists(productId);
        isOptionNameExists(productId, optionDTO.getName());

        Option option = getOptionById(optionDTO.getId());
        option.updateOption(optionDTO);
        optionRepository.save(option);
    }

    public void deleteOption(long productId, long optionId) {
        isProductExists(productId);

        Option option = getOptionById(optionId);
        optionRepository.delete(option);
    }

    private void isProductExists(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND);
        }
    }

    private void isOptionNameExists(long productId, String optionName) {
        if (optionRepository.existsByNameAndProductId(optionName, productId)) {
            throw new IllegalArgumentException(OPTION_ALREADY_EXISTS);
        }
    }

    private Option getOptionById(long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException(OPTION_NOT_FOUND));
    }
}
